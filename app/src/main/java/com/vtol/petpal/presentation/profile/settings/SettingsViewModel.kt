package com.vtol.petpal.presentation.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.worker.SyncScheduler
import com.vtol.petpal.domain.repository.SettingsRepository
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: AppUseCases,
    private val repository: SettingsRepository,
    private val syncScheduler: SyncScheduler
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

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

            }.catch { e ->
                _state.update { it.copy(error = e.message) }
            }
            .launchIn(viewModelScope)
    }

    private fun onCloudSyncToggled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setCloudSyncEnabled(enabled)

            if (enabled) syncScheduler.scheduleSync()
            else syncScheduler.cancelSync()
        }
    }

    fun isNotificationEnabled() {
        useCases.getNotificationStatus()
            .onEach { enabled ->
                _state.update { it.copy(isNotificationEnabled = enabled) }

            }
            .catch { e ->
                _state.update { it.copy(error = e.message) }
            }.launchIn(viewModelScope)
    }

    fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            useCases.toggleNotification(enabled)
        }
    }
}

data class SettingsUiState(
    val isNotificationEnabled: Boolean = true,
    val isSyncEnabled: Boolean = false,
    val error: String? = null
)