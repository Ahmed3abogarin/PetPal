package com.vtol.petpal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.TaskUi
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
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()


    init {
        observeHomeData()
    }

    private fun observeHomeData() {
        combine(appUseCases.getTasks(), appUseCases.getPets()) { tasks, pets ->
            // Success logic...

            val petMap = pets.associate { pet -> pet.id to pet.petName }
            val todayTasksList = todayTasks(tasks)  // extract first

            val total = todayTasksList.size                                    // ← was tasks.size
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
                // Handle the error here!
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
                        // Preserve these flags from the current UI state
                        taskSaved = currentState.taskSaved,
                        showNotificationPermissionDialog = currentState.showNotificationPermissionDialog,
                        showExactAlarmPermissionDialog = currentState.showExactAlarmPermissionDialog
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun toggleCompletion(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            appUseCases.toggleTask(taskId, isCompleted)
        }
    }

    fun todayTasks(tasks: List<TaskUi>) = tasks.filter {
        val taskDate =
            Instant.ofEpochMilli(it.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()
        taskDate == LocalDate.now()
    }

    fun upcomingTasks(tasks: List<TaskUi>) = tasks.filter {
        val taskDate =
            Instant.ofEpochMilli(it.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()
        taskDate.isAfter(LocalDate.now())
    }
}

data class HomeState(
    val todayTasks: List<TaskUi> = emptyList(),
    val upcomingTasks: List<TaskUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val petMap: Map<String, String> = emptyMap(),
    val petsList: List<Pet> = emptyList(),
    val showNotificationPermissionDialog: Boolean = false,
    val showExactAlarmPermissionDialog: Boolean = false,
    val taskSaved: Boolean = false,

    val completedCount: Int = 0,
    val total: Int = 0,
    val progress: Float = 0f,
    val percentage: Int = 0,
)