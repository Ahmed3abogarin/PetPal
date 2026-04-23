package com.vtol.petpal.presentation.home

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.notification.NotificationPermissionManager
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val permissionManager: NotificationPermissionManager,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _permissionRequired = MutableStateFlow(false)
    val permissionRequired = _permissionRequired.asStateFlow()

    // Hold the task temporarily while we ask for permissions
    private var pendingTask: Task? = null

    init {
        observeHomeData()
    }

    fun insertTask(task: Task) {
        pendingTask = task // Store it!

        viewModelScope.launch {
            when {
                !permissionManager.hasNotificationPermission() -> {
                    _state.update {
                        it.copy(showNotificationPermissionDialog = true)
                    }
                }
                !permissionManager.hasExactAlarmPermission() -> {
                    _state.update {
                        it.copy(showExactAlarmPermissionDialog = true)
                    }
                }
                else -> saveTask(task)
            }

            // Check if exact alarm permission is needed and notify UI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = context.getSystemService(AlarmManager::class.java)
                if (!alarmManager.canScheduleExactAlarms()) {
                    _permissionRequired.update { true }
                }
            }
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            appUseCases.insertTask(task)
            pendingTask = null // clear it out
            _state.update { it.copy(taskSaved = true) } // Trigger UI navigation
        }
    }

    fun resetTaskSaved() {
        _state.update { it.copy(taskSaved = false) }
    }

    fun onPermissionHandled() {
        _permissionRequired.update { false }
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


    fun todayTasks(tasks: List<Task>) = tasks.filter {
        val taskDate =
            Instant.ofEpochMilli(it.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()
        taskDate == LocalDate.now()
    }

    fun upcomingTasks(tasks: List<Task>) = tasks.filter {
        val taskDate =
            Instant.ofEpochMilli(it.dateTime).atZone(ZoneId.systemDefault()).toLocalDate()
        taskDate.isAfter(LocalDate.now())
    }



    // Permission Flow Handlers
    fun onNotificationPermissionResult(granted: Boolean) {
        _state.update { it.copy(showNotificationPermissionDialog = false) }
        val task = pendingTask ?: return

        when {
            !granted -> saveTask(task)
            !permissionManager.hasExactAlarmPermission() -> {
                _state.update { it.copy(showExactAlarmPermissionDialog = true) }
            }
            else -> saveTask(task)
        }
    }

    fun onNotificationPermissionDismissed() {
        _state.update { it.copy(showNotificationPermissionDialog = false) }
        pendingTask?.let { saveTask(it) }
    }

    fun onExactAlarmPermissionResult() {
        _state.update { it.copy(showExactAlarmPermissionDialog = false) }
        pendingTask?.let { saveTask(it) }
    }

    fun onPermissionDismissed() {
        _state.update { it.copy(showExactAlarmPermissionDialog = false) }
        pendingTask?.let { saveTask(it) }
    }

}

data class HomeState(
    val todayTasks: List<Task> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
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