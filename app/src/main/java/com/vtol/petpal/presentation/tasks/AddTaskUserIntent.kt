package com.vtol.petpal.presentation.tasks

import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.TaskType
import java.time.LocalDate
import java.time.LocalTime

sealed interface AddTaskUserIntent {
    data class FoodChanged(val text: String) : AddTaskUserIntent
    data class AmountChanged(val text: String) : AddTaskUserIntent

    data class DateChanged(val date: LocalDate) : AddTaskUserIntent
    data class TimeChanged(val time: LocalTime) : AddTaskUserIntent

    data class ClinicChanged(val text: String) : AddTaskUserIntent
    data class ReasonChanged(val text: String) : AddTaskUserIntent

    data class RecurrenceChanged(val repeat: RepeatInterval) : AddTaskUserIntent
    data class MedNameChanged(val text: String) : AddTaskUserIntent
    data class DosageChanged(val text: String) : AddTaskUserIntent
    data class DurationChanged(val text: String) : AddTaskUserIntent
    data class LocationChanged(val text: String) : AddTaskUserIntent
    data class NoteChanged(val text: String) : AddTaskUserIntent
    data class TypeSelected(val type: TaskType) : AddTaskUserIntent
    data class PetSelected(val pet: Pet) : AddTaskUserIntent
    object LogScreenView : AddTaskUserIntent
    object SaveClicked : AddTaskUserIntent

    // Permission specific intents
    data class NotificationPermissionResult(val granted: Boolean) : AddTaskUserIntent
    object ExactAlarmPermissionResult : AddTaskUserIntent
    object DismissNotificationDialog : AddTaskUserIntent
    object DismissExactAlarmDialog : AddTaskUserIntent
}

// ViewModel -> UI (One-Shot Effects)
sealed interface AddTaskUiEffect {
    object NavigateUp : AddTaskUiEffect
    data class ShowSnackbar(val message: String) : AddTaskUiEffect
}