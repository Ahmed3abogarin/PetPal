package com.vtol.petpal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.worker.SyncScheduler
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.user.User
import com.vtol.petpal.domain.repository.SettingsRepository
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.premium.IsPremiumUseCase
import com.vtol.petpal.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    private val syncScheduler: SyncScheduler,
    settingsRepository: SettingsRepository,
    isPremiumUseCase: IsPremiumUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val isPremium = isPremiumUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val isCloudSyncEnabled = settingsRepository.isCloudSyncEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        observeHomeData()
        getUser()
        viewModelScope.launch {
            // wait for both flows to emit their first value
            if (isPremium.value && isCloudSyncEnabled.value) {
                syncScheduler.scheduleSync()
            }
        }
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

    // --- PRODUCTION READY FILTERING LOGIC ---

    fun todayTasks(tasks: List<TaskUi>): List<TaskUi> {
        val today = LocalDate.now()

        return tasks.filter { task ->
            // Use your utility extension to ensure identical timezone parsing across screens
            val taskDate = task.dateTime.toLocalDate()

            val occursToday = when (task.repeatInterval ?: RepeatInterval.Never) {
                RepeatInterval.Never -> taskDate == today
                RepeatInterval.Daily -> taskDate <= today
                RepeatInterval.Weekly -> taskDate <= today && ChronoUnit.DAYS.between(taskDate, today) % 7 == 0L
                RepeatInterval.Monthly -> taskDate <= today && taskDate.dayOfMonth == today.dayOfMonth
            }

            occursToday && !task.deletedDates.contains(today)
        }
    }

    fun upcomingTasks(tasks: List<TaskUi>): List<TaskUi> {
        val today = LocalDate.now()
        // Define a visible window for "Upcoming" on the Home Screen (e.g., the next 7 days)
        val upcomingWindowEnd = today.plusDays(7)

        return tasks.filter { task ->
            val taskDate = task.dateTime.toLocalDate()

            // Check if this task hits ANY valid repeating day in the next 7 days
            var hasUpcomingOccurrence = false
            var checkDate = today.plusDays(1)

            while (checkDate <= upcomingWindowEnd) {
                val occursOnCheckDate = when (task.repeatInterval ?: RepeatInterval.Never) {
                    RepeatInterval.Never -> taskDate == checkDate
                    RepeatInterval.Daily -> taskDate <= checkDate
                    RepeatInterval.Weekly -> taskDate <= checkDate && ChronoUnit.DAYS.between(taskDate, checkDate) % 7 == 0L
                    RepeatInterval.Monthly -> taskDate <= checkDate && taskDate.dayOfMonth == checkDate.dayOfMonth
                }

                if (occursOnCheckDate && !task.deletedDates.contains(checkDate)) {
                    hasUpcomingOccurrence = true
                    break // Found one! No need to keep looping for this task
                }
                checkDate = checkDate.plusDays(1)
            }

            hasUpcomingOccurrence
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