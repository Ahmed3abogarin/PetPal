package com.vtol.petpal.presentation.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.util.AnalyticsParams.CALENDAR_SCREEN
import com.vtol.petpal.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper
) : ViewModel() {
    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    init {
        getCalendarTasks()
        getPets()
    }

    fun getPets() {
        appUseCases.getPets()
            .onStart {
                _state.value = _state.value.copy(isLoading = true, error = null)
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load pets"
                )
            }
            .onEach { pets ->
                val petMap = pets.associate { pet -> pet.id to pet.petName }
                _state.value = _state.value.copy(
                    pets = pets,
                    petMap = petMap,
                    isLoading = false,
                    error = null
                )
            }
            .launchIn(viewModelScope)
    }

    fun getCalendarTasks() {
        viewModelScope.launch {
            appUseCases.getTasks().collect { tasks ->
                val start = YearMonth.now().minusMonths(6).atDay(1)
                val end = YearMonth.now().plusMonths(12).atEndOfMonth()
                _state.update {
                    it.copy(tasks = generateCalendarTasks(tasks, start, end))
                }
            }
        }
    }

    private fun generateCalendarTasks(
        tasks: List<TaskUi>,
        start: LocalDate,
        end: LocalDate
    ): Map<LocalDate, List<TaskUi>> {

        val result = mutableMapOf<LocalDate, MutableList<TaskUi>>()

        tasks.forEach { task ->
            val taskDate = task.dateTime.toLocalDate()

            when (task.repeatInterval ?: RepeatInterval.Never) {

                RepeatInterval.Never -> {
                    // Check if this single date was marked as deleted
                    if (taskDate in start..end && !task.deletedDates.contains(taskDate)) {
                        result.getOrPut(taskDate) { mutableListOf() }.add(task)
                    }
                }

                RepeatInterval.Daily -> {
                    var date = maxOf(taskDate, start)
                    while (date <= end) {
                        // Skip adding if the date is in the deleted exclusions list
                        if (!task.deletedDates.contains(date)) {
                            result.getOrPut(date) { mutableListOf() }.add(task)
                        }
                        date = date.plusDays(1)
                    }
                }

                RepeatInterval.Weekly -> {
                    var date = taskDate
                    while (date < start) date = date.plusWeeks(1)
                    while (date <= end) {
                        if (!task.deletedDates.contains(date)) {
                            result.getOrPut(date) { mutableListOf() }.add(task)
                        }
                        date = date.plusWeeks(1)
                    }
                }

                RepeatInterval.Monthly -> {
                    var date = taskDate
                    while (date < start) date = date.plusMonths(1)
                    while (date <= end) {
                        if (!task.deletedDates.contains(date)) {
                            result.getOrPut(date) { mutableListOf() }.add(task)
                        }
                        date = date.plusMonths(1)
                    }
                }
            }
        }

        return result
    }

    // Option 1: User chose "Delete All Occurrences"
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            appUseCases.deleteTask(taskId)
        }
    }

    // Option 2: User chose "Delete Just This Occurrence"
    fun deleteSpecificOccurrence(task: TaskUi, dateToDelete: LocalDate) {
        viewModelScope.launch {
            if (task.repeatInterval == null || task.repeatInterval == RepeatInterval.Never) {
                // It's a single event, safe to permanently delete from DB
                appUseCases.deleteTask(task.id)
            } else {
                // It's a repeating event, add this date to the exclusions list
                val updatedDates = task.deletedDates + dateToDelete
                val updatedTask = task.copy(deletedDates = updatedDates)

                // Save the updated task to the database
                appUseCases.updateTask(updatedTask)
            }
        }
    }

    fun logScreenView(){
        firebaseAnalyticsHelper.logScreenView(CALENDAR_SCREEN)
    }
}

data class CalendarState(
    val tasks: Map<LocalDate, List<TaskUi>> = emptyMap(),
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val petMap: Map<String, String> = emptyMap(),
    val error: String? = null
)