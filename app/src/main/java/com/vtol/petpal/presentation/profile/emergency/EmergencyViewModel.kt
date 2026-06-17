package com.vtol.petpal.presentation.profile.emergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.data.repository.FirebaseAnalyticsHelper
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.usecases.emergency.EmergencyEvent
import com.vtol.petpal.domain.usecases.emergency.EmergencyUseCases
import com.vtol.petpal.util.AnalyticsParams.EMERGENCY_SCREEN
import com.vtol.petpal.util.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel @Inject constructor(
    private val useCases: EmergencyUseCases,
    private val firebaseAnalyticsHelper: FirebaseAnalyticsHelper
) : ViewModel() {

    private val _state = MutableStateFlow(EmergencyUiState())
    val state = _state.asStateFlow()

    private val _sheet = MutableStateFlow<EmergencySheet?>(null)
    val sheet: StateFlow<EmergencySheet?> = _sheet.asStateFlow()

    init {
        observeContacts()
    }

    fun onEvent(event: EmergencyEvent) {
        when (event) {

            // Navigation
            is EmergencyEvent.OpenAdd -> _sheet.value = EmergencySheet.Add
            is EmergencyEvent.OpenMore -> _sheet.value = EmergencySheet.More(event.contact)
            is EmergencyEvent.OpenEdit -> _sheet.value = EmergencySheet.Edit(event.contact)
            is EmergencyEvent.OpenDelete -> _sheet.value = EmergencySheet.Delete(event.contact)
            is EmergencyEvent.OpenDetails -> _sheet.value = EmergencySheet.Details(event.contact)
            is EmergencyEvent.DismissSheet -> _sheet.value = null


            // CRUD
            is EmergencyEvent.AddContact -> addContact(event.contact)
            is EmergencyEvent.DeleteContact -> deleteContact(event.contact)
            is EmergencyEvent.UpdateContact -> updateContact(event.contact)
            is EmergencyEvent.ErrorShown -> _state.update { it.copy(message = null) }

            is EmergencyEvent.LogScreenView -> firebaseAnalyticsHelper.logScreenView(EMERGENCY_SCREEN)
        }
    }

    private fun observeContacts() {
        useCases.observeContacts()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { list -> _state.update { it.copy(isLoading = false, contacts = list) } }
            .catch { e -> _state.update { it.copy(isLoading = false, message = e.message) } }
            .launchIn(viewModelScope)

    }

    private fun addContact(contact: EmergencyContact) {

        val nameResult = ValidationUtils.validateName(contact.name)
        val numberResult = ValidationUtils.validatePhone(contact.phoneNumber)

        val hasError = listOf(nameResult, numberResult).any { it != null }

        if (hasError) {
            _state.update {
                it.copy(
                    nameError = nameResult,
                    phoneError = numberResult
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true) }
            useCases.addContact(contact)
                .onSuccess {
                    _sheet.value = null
                    _state.update { it.copy(isSheetLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSheetLoading = false, message = e.message) }
                }
        }

    }

    private fun updateContact(contact: EmergencyContact) {
        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true) }
            useCases.updateContact(contact)
                .onSuccess {
                    _sheet.value = null
                    _state.update { it.copy(isSheetLoading = false, message = "Updated successfully" ) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSheetLoading = false, message = e.message) }
                }
        }
    }

    private fun deleteContact(contact: EmergencyContact) {
        viewModelScope.launch {
            _state.update { it.copy(isSheetLoading = true) }
            useCases.deleteContact(contact)
                .onSuccess {
                    _sheet.value = null
                    _state.update { it.copy(isSheetLoading = false, message = "${contact.name} removed") }
                }
                .onFailure { e ->
                    _state.update { it.copy(isSheetLoading = false, message = e.message) }
                }
        }
    }
}

data class EmergencyUiState(
    val isLoading: Boolean = false,
    val isSheetLoading: Boolean = false,

    val contacts: List<EmergencyContact> = emptyList(),
    val message: String? = null,
    val nameError: String? = null,
    val phoneError: String? = null
)