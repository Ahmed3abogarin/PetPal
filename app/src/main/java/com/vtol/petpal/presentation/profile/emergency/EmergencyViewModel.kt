package com.vtol.petpal.presentation.profile.emergency

import androidx.lifecycle.ViewModel
import com.vtol.petpal.domain.model.ContactType
import com.vtol.petpal.domain.model.EmergencyContact
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel @Inject constructor(

    private val appUseCases: AppUseCases
): ViewModel() {

    private val _state = MutableStateFlow(EmergencyUiState())
    val state = _state.asStateFlow()


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