package com.vtol.petpal.presentation.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.WalkDetails
import com.vtol.petpal.domain.usecases.AppUseCases
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
    private val appUseCases: AppUseCases
): ViewModel() {
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

    fun getCalendarTasks(){
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

    fun generateCalendarTasks(
        tasks: List<TaskUi>,
        start: LocalDate,
        end: LocalDate
    ): Map<LocalDate, List<TaskUi>> {

        val result = mutableMapOf<LocalDate, MutableList<TaskUi>>()

        tasks.forEach { task ->
            val taskDate = task.dateTime.toLocalDate()

            when (task.repeatInterval ?: RepeatInterval.Never) {

                RepeatInterval.Never -> {
                    if (taskDate in start..end) {
                        result.getOrPut(taskDate) { mutableListOf() }.add(task)
                    }
                }

                RepeatInterval.Daily -> {
                    var date = maxOf(taskDate, start)
                    while (date <= end) {
                        result.getOrPut(date) { mutableListOf() }.add(task)
                        date = date.plusDays(1)
                    }
                }

                RepeatInterval.Weekly -> {
                    var date = taskDate
                    while (date < start) date = date.plusWeeks(1)
                    while (date <= end) {
                        result.getOrPut(date) { mutableListOf() }.add(task)
                        date = date.plusWeeks(1)
                    }
                }

                RepeatInterval.Monthly -> {
                    var date = taskDate
                    while (date < start) date = date.plusMonths(1)
                    while (date <= end) {
                        result.getOrPut(date) { mutableListOf() }.add(task)
                        date = date.plusMonths(1)
                    }
                }
            }
        }

        return result
    }

}

val previewTasks = mapOf(
    LocalDate.of(2026, 5, 20) to listOf(
        TaskUi(
            id = 5L,
            petId = "dog_01",
            title = "Quick Walk",
            note = null,
            type = TaskType.WALK,
            dateTime = 1779287687000L, // Static timestamp matching date
            isCompleted = false,
            repeatInterval = null,
            details = WalkDetails( 22, "Neighborhood Block"),
            syncStatus = SyncStatus.SYNCED
        ),
        TaskUi(
            id = 5L,
            petId = "dog_01",
            title = "Quick Walk",
            note = null,
            type = TaskType.WALK,
            dateTime = 1779287687000L, // Static timestamp matching date
            isCompleted = false,
            repeatInterval = null,
            details = WalkDetails( 22, "Neighborhood Block"),
            syncStatus = SyncStatus.SYNCED
        ),
        TaskUi(
            id = 5L,
            petId = "dog_01",
            title = "Quick Walk",
            note = null,
            type = TaskType.WALK,
            dateTime = 1779287687000L, // Static timestamp matching date
            isCompleted = false,
            repeatInterval = null,
            details = WalkDetails( 22, "Neighborhood Block"),
            syncStatus = SyncStatus.SYNCED
        ),
        TaskUi(
            id = 5L,
            petId = "dog_01",
            title = "Quick Walk",
            note = null,
            type = TaskType.WALK,
            dateTime = 1779287687000L, // Static timestamp matching date
            isCompleted = false,
            repeatInterval = null,
            details = WalkDetails( 22, "Neighborhood Block"),
            syncStatus = SyncStatus.SYNCED
        ),
        TaskUi(
            id = 5L,
            petId = "dog_01",
            title = "Quick Walk",
            note = null,
            type = TaskType.WALK,
            dateTime = 1779287687000L, // Static timestamp matching date
            isCompleted = false,
            repeatInterval = null,
            details = WalkDetails( 22, "Neighborhood Block"),
            syncStatus = SyncStatus.SYNCED
        )
    ),
)

data class CalendarState(
    val tasks: Map<LocalDate, List<TaskUi>> = previewTasks,
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val petMap: Map<String, String> = emptyMap(),
    val error: String? = null
)