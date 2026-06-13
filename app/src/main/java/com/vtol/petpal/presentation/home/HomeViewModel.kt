package com.vtol.petpal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval // <-- Added Import
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()


    init {
        observeHomeData()
        getUser()
    }

    private fun observeHomeData() {
        combine(appUseCases.getTasks(), appUseCases.getPets()) { tasks, pets ->
            val petMap = pets.associate { pet -> pet.id to pet.petName }
            val todayTasksList = todayTasks(tasks)

            val total = todayTasksList.size
            val completed = todayTasksList.count { task -> task.isCompleted }

            val progress = if (total > 0) completed.toFloat() / total else 0f

            HomeState(
                todayTasks = todayTasksList,
                petsList = pets,
                petMap = petMap,
                upcomingTasks = upcomingTasks(tasks),
                completedCount = completed,
                progress = progress,
                total = total,
                percentage = (progress * 100).toInt()
            )
        }
            .onStart { _state.update { it.copy(isLoading = true) } }
            .catch { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load pets: ${exception.message}"
                    )
                }
            }
            .onEach { dataUpdate ->
                _state.update { currentState ->
                    dataUpdate.copy(
                        taskSaved = currentState.taskSaved,
                        showNotificationPermissionDialog = currentState.showNotificationPermissionDialog,
                        showExactAlarmPermissionDialog = currentState.showExactAlarmPermissionDialog,
                        isUserLoading = currentState.isUserLoading,
                        user = currentState.user
                    )
                }
            }.launchIn(viewModelScope)
    }


    private fun getUser() {
        viewModelScope.launch {
            appUseCases.getUser()
                .catch { e -> _state.update { it.copy(isUserLoading = false, error = e.message) } }
                .collect { user -> _state.update { it.copy(isUserLoading = false, user = user) } }
        }
    }

    fun toggleCompletion(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            appUseCases.toggleTask(taskId, isCompleted)
        }
    }

    // --- UPDATED FILTERING LOGIC ---

    fun todayTasks(tasks: List<TaskUi>): List<TaskUi> {
        val today = LocalDate.now()

        return tasks.filter { task ->
            val taskDate = Instant.ofEpochMilli(task.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()

            // 1. Calculate if this task repeats or falls on today's date
            val occursToday = when (task.repeatInterval ?: RepeatInterval.Never) {
                RepeatInterval.Never -> taskDate == today
                RepeatInterval.Daily -> taskDate <= today
                RepeatInterval.Weekly -> taskDate <= today && ChronoUnit.DAYS.between(taskDate, today) % 7 == 0L
                RepeatInterval.Monthly -> taskDate <= today && taskDate.dayOfMonth == today.dayOfMonth
            }

            // 2. Filter out if today's date exists in the deleted exceptions list
            occursToday && !task.deletedDates.contains(today)
        }
    }

    fun upcomingTasks(tasks: List<TaskUi>): List<TaskUi> {
        val today = LocalDate.now()

        return tasks.filter { task ->
            val taskDate = Instant.ofEpochMilli(task.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()

            // Only capture future tasks and ensure their specific target date wasn't deleted
            taskDate.isAfter(today) && !task.deletedDates.contains(taskDate)
        }
    }
}

data class HomeState(
    val todayTasks: List<TaskUi> = emptyList(),
    val upcomingTasks: List<TaskUi> = emptyList(),
    val isLoading: Boolean = false,
    val isUserLoading: Boolean = true,
    val error: String? = null,
    val petMap: Map<String, String> = emptyMap(),
    val petsList: List<Pet> = emptyList(),
    val showNotificationPermissionDialog: Boolean = false,
    val showExactAlarmPermissionDialog: Boolean = false,
    val taskSaved: Boolean = false,
    val user: User? = null,

    val completedCount: Int = 0,
    val total: Int = 0,
    val progress: Float = 0f,
    val percentage: Int = 0,
)