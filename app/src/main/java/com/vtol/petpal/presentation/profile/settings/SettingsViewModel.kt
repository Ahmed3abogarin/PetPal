package com.vtol.petpal.presentation.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.data.worker.SyncScheduler
import com.vtol.petpal.domain.repository.SettingsRepository
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.premium.IsPremiumUseCase
import com.vtol.petpal.domain.usecases.tasks.RestoreTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: AppUseCases,
    private val repository: SettingsRepository,
    private val syncScheduler: SyncScheduler,
    private val restoreTasksUseCase: RestoreTasksUseCase,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper,
    isPremiumUseCase: IsPremiumUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    private val isPremium = isPremiumUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    private val _uiEffect = Channel<SettingsUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()


    init {
        isNotificationEnabled()
        isCloudSyncEnabled()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnToggleNotification -> toggleNotification(event.isEnabled)
            is SettingsEvent.OnToggleSyncUp -> onCloudSyncToggled(event.isEnabled)
        }
    }

    private fun isCloudSyncEnabled() {
        repository.isCloudSyncEnabled()
            .onEach { enabled ->
                _state.update { it.copy(isSyncEnabled = enabled) }
            }
            .catch { e ->
                _state.update { it.copy(error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun onCloudSyncToggled(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled && !isPremium.value) {
                // navigate the user to premium subscriptions
                // redirect to paywall instead of enabling
                _uiEffect.send(SettingsUiEffect.NavigateToPremium)
                return@launch
            }

            repository.setCloudSyncEnabled(enabled)

            if (enabled) {
                restoreTasks()
                syncScheduler.scheduleSync()
            } else {
                syncScheduler.cancelSync()
            }
        }
    }

    private suspend fun restoreTasks() {
        _state.update { it.copy(isRestoring = true, error = null) }

        restoreTasksUseCase()
            .onSuccess { count ->
                _state.update { it.copy(isRestoring = false, restoredCount = count) }
            }
            .onFailure { e ->
                _state.update { it.copy(isRestoring = false, error = e.message) }
            }
    }

    private fun isNotificationEnabled() {
        useCases.getNotificationStatus()
            .onEach { enabled ->
                _state.update { it.copy(isNotificationEnabled = enabled) }
            }
            .catch { e ->
                _state.update { it.copy(error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.toggleNotification(enabled)
        }
    }

    fun logScreenView(screenName: String){
        firebaseAnalyticsHelper.logScreenView(screenName)
    }
}

data class SettingsUiState(
    val isNotificationEnabled: Boolean = true,
    val isSyncEnabled: Boolean = false,
    val isRestoring: Boolean = false,
    val restoredCount: Int? = null,
    val error: String? = null
)

sealed class SettingsUiEffect {
    object NavigateToPremium : SettingsUiEffect()
}