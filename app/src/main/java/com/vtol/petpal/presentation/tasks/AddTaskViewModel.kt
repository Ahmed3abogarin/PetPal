package com.vtol.petpal.presentation.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.vtol.petpal.data.notification.NotificationPermissionManager
import com.vtol.petpal.data.worker.SyncScheduler
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.tasks.RepeatInterval
import com.vtol.petpal.domain.model.tasks.Task
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.details.FoodDetails
import com.vtol.petpal.domain.model.tasks.details.MedDetails
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.domain.model.tasks.details.WalkDetails
import com.vtol.petpal.domain.repository.SettingsRepository
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

// Immutable state holding the form and permission UI status
data class AddTaskState(
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null,
    val selectedType: TaskType? = null,
    val dueDate: LocalDate ? = null,
    val dueTime: LocalTime? = null,
    val recurrence: RepeatInterval = RepeatInterval.Never,
    val recurrenceIndex: Int = -1,

    // Dynamic Fields
    val food: String = "",
    val amount: String = "",
    val clinic: String = "",
    val reason: String = "",
    val medicineName: String = "",
    val dosage: String = "",
    val duration: String = "",
    val location: String = "",
    val note: String = "",

    // UI Status
    val isLoading: Boolean = false,
    val isPetsLoading: Boolean = false,
    val taskSaved: Boolean = false,
    val errorMessage: String? = null,
    val showNotificationDialog: Boolean = false,
    val showExactAlarmDialog: Boolean = false
) {
    val isFormValid: Boolean
        get() {
            if (selectedPet == null) return false
            if (selectedType == null) return false
            if (dueDate == null) return false
            if (dueTime == null) return false

            return when (selectedType) {
                TaskType.FEED -> food.isNotBlank() && amount.isNotBlank()
                TaskType.VET -> clinic.isNotBlank()          // reason is optional
                TaskType.MEDICATION -> medicineName.isNotBlank() && dosage.isNotBlank()
                TaskType.WALK -> location.isNotBlank() && duration.isNotBlank()
            }
        }
}

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    private val permissionManager: NotificationPermissionManager,
    private val settingsRepository: SettingsRepository,
    private val syncScheduler: SyncScheduler,
    savedStateHandle: SavedStateHandle,
    private val gson: Gson
) : ViewModel() {

    private val _state = MutableStateFlow(AddTaskState())
    val state = _state.asStateFlow()

    private val _uiEffect = Channel<AddTaskUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    private val petId: String = checkNotNull(savedStateHandle["petId"])
    private var pendingTask: Task? = null

    init {
        getPets()
    }

    // Single pipeline entry point matching your other screens
    fun onIntent(intent: AddTaskUserIntent) {
        when (intent) {
            is AddTaskUserIntent.PetSelected -> _state.update { it.copy(selectedPet = intent.pet) }
            is AddTaskUserIntent.TypeSelected -> _state.update {
                it.copy(
                    selectedType = intent.type,
                    food         = "",
                    amount       = "",
                    clinic       = "",
                    reason       = "",
                    medicineName = "",
                    dosage       = "",
                    location     = "",
                    duration     = "",
                )
            }
            is AddTaskUserIntent.FoodChanged -> _state.update { it.copy(food = intent.text) }
            is AddTaskUserIntent.AmountChanged -> _state.update { it.copy(amount = intent.text) }
            is AddTaskUserIntent.ClinicChanged -> _state.update { it.copy(clinic = intent.text) }
            is AddTaskUserIntent.ReasonChanged -> _state.update { it.copy(reason = intent.text) }
            is AddTaskUserIntent.MedNameChanged -> _state.update { it.copy(medicineName = intent.text) }
            is AddTaskUserIntent.DosageChanged -> _state.update { it.copy(dosage = intent.text) }
            is AddTaskUserIntent.LocationChanged -> _state.update { it.copy(location = intent.text) }
            is AddTaskUserIntent.NoteChanged -> _state.update { it.copy(note = intent.text) }
            is AddTaskUserIntent.DurationChanged -> _state.update { it.copy(duration = intent.text) }
            is AddTaskUserIntent.RecurrenceChanged -> _state.update { it.copy(recurrence = intent.repeat) }
            is AddTaskUserIntent.DateChanged -> _state.update { it.copy(dueDate = intent.date) }
            is AddTaskUserIntent.TimeChanged -> _state.update { it.copy(dueTime = intent.time) }
            AddTaskUserIntent.SaveClicked -> submitTask()

            // Handlers for dynamic dialogues route cleanly here
            is AddTaskUserIntent.NotificationPermissionResult -> handleNotificationPermission(intent.granted)
            AddTaskUserIntent.ExactAlarmPermissionResult -> handleExactAlarmPermission()
            AddTaskUserIntent.DismissNotificationDialog -> dismissNotificationDialog()
            AddTaskUserIntent.DismissExactAlarmDialog -> dismissExactAlarmDialog()
        }
    }

    fun getPets() {
        appUseCases.getPets()
            .onStart {
                _state.value = _state.value.copy(isPetsLoading = true, errorMessage = null)
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isPetsLoading = false,
                    errorMessage = e.message ?: "Failed to load pets"
                )
            }
            .onEach { pets ->
                if (petId.isNotBlank()){
                    _state.update { it.copy(selectedPet = pets.first { pet -> pet.id == petId }) }
                }
                _state.value = _state.value.copy(
                    pets = pets,
                    isPetsLoading = false,
                    errorMessage = null
                )
            }
            .launchIn(viewModelScope)
    }

    private fun submitTask() {
        val s = _state.value
        Timber.tag("AddTaskVM").e("${s.selectedType}, ${s.selectedPet?.petName}")
        if (!s.isFormValid) return

        val pet = s.selectedPet ?: return
        val type = s.selectedType ?: return
        val date = s.dueDate ?: return
        val time = s.dueTime ?: return

        _state.update { it.copy(isLoading = true) }

        Timber.tag("AddTaskVM").e("WE ARE SAFE!!!!!!")


        val jsonDetails = when (s.selectedType) {
            TaskType.FEED -> gson.toJson(FoodDetails(s.food, s.amount))
            TaskType.VET -> gson.toJson(VetDetails(s.clinic, s.reason))
            TaskType.MEDICATION -> gson.toJson(MedDetails(s.medicineName, s.dosage))
            TaskType.WALK -> gson.toJson(WalkDetails(s.duration.toIntOrNull() ?: 0, s.location))
        }

        val combinedDateTime = LocalDateTime.of(date, time)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        pendingTask = Task(
            petId = pet.id,
            title = type.name,
            type = s.selectedType,
            dateTime = combinedDateTime,
            details = jsonDetails,
            repeatInterval = s.recurrence,
            note = s.note,
            isCompleted = false
        )
        checkPermissionsAndSave()
    }

    private fun checkPermissionsAndSave() {
        Timber.tag("AddTaskVM").e("calling check permission")

        viewModelScope.launch {
            when {
                !permissionManager.hasNotificationPermission() -> {

                    Timber.tag("AddTaskVM").e("!hasNotificationPermission")

                    _state.update { it.copy(showNotificationDialog = true) }

                }

                !permissionManager.hasExactAlarmPermission() -> {
                    Timber.tag("AddTaskVM").e("!hasExactAlarmPermission")

                    _state.update { it.copy(showExactAlarmDialog = true) }
                }

                else -> pendingTask?.let { saveTask(it) }
            }
        }
    }

    private fun saveTask(task: Task) {
        Timber.tag("AddTaskVM").e("calling save Task !!!????")

        viewModelScope.launch {
            try {
                appUseCases.insertTask(task, state.value.selectedPet?.petName ?: "your pet")
                if (settingsRepository.isCloudSyncEnabled().first()) {
                    syncScheduler.scheduleSync() // then trigger background sync
                }
                pendingTask = null
                _state.update { it.copy(isLoading = false) }
                _uiEffect.send(AddTaskUiEffect.NavigateUp)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _uiEffect.send(AddTaskUiEffect.ShowSnackbar("Failed to save: ${e.localizedMessage}"))
            }
        }
    }

    private fun handleNotificationPermission(granted: Boolean) {
        if (!granted) {
            // User denied notifications. Do not check for exact alarms, just clear dialog and save task.
            _state.update { it.copy(showNotificationDialog = false) }
            pendingTask?.let { saveTask(it) }
        } else {
            // User granted notifications. Synchronously look up if we need exact alarm permissions.
            val needsExactAlarm = !permissionManager.hasExactAlarmPermission()

            _state.update { currentState ->
                currentState.copy(
                    showNotificationDialog = false,
                    showExactAlarmDialog = needsExactAlarm
                )
            }

            // If exact alarm permission is already granted, proceed directly to save
            if (!needsExactAlarm) {
                pendingTask?.let { saveTask(it) }
            }
        }
    }

    private fun handleExactAlarmPermission() {
        _state.update { it.copy(showExactAlarmDialog = false) }
        pendingTask?.let { saveTask(it) }
    }

    private fun dismissNotificationDialog() {
        // User dismissed notification rational. If they don't want notifications,
        // we skip exact alarm setup entirely and just save.
        _state.update { it.copy(showNotificationDialog = false) }
        pendingTask?.let { saveTask(it) }
    }

    private fun dismissExactAlarmDialog() {
        _state.update { it.copy(showExactAlarmDialog = false) }
        pendingTask?.let { saveTask(it) }
    }
}