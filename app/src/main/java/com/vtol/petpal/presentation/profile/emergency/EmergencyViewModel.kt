package com.vtol.petpal.presentation.profile.emergency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.usecases.AppUseCases
import com.vtol.petpal.domain.usecases.emergency.EmergencyUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val useCases: EmergencyUseCases
) : ViewModel() {

    // TODO: Validate the inputs first before sending them to firestore
    private val _state = MutableStateFlow(EmergencyUiState())
    val state = _state.asStateFlow()

    init {
        observeContacts()
    }

    private fun onEvent(){

    }

    private fun observeContacts() {
        useCases.observeContacts()
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { list -> _state.update { it.copy(isLoading = false, contacts = list) } }
            .catch { e -> _state.update { it.copy(isLoading = false, error = e.message) } }
            .launchIn(viewModelScope)

    }

    private fun addContact() {

    }

    private fun updateContact(){

    }

    private fun deleteContact() {

    }


}

data class EmergencyUiState(
    val isLoading: Boolean = false,
    val contacts: List<EmergencyContact> = listOf(
        EmergencyContact(
            "",
            "Dr.Phill",
            notes = "gsdgsgsgklj",
            phoneNumber = "+966560523130",
            relationship = ContactType.PET_SITTER
        )
    ),
    val error: String? = null
)