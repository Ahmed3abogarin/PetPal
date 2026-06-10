package com.vtol.petpal.presentation.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val useCases: AppUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    init {
        isNotificationEnabled()
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
    val error: String? = null
)