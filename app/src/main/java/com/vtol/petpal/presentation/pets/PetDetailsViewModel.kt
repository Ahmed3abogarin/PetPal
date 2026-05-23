package com.vtol.petpal.presentation.pets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.weight.WeightRange
import com.vtol.petpal.domain.model.WeightRecord
import com.vtol.petpal.domain.model.tasks.SyncStatus
import com.vtol.petpal.domain.model.tasks.TaskType
import com.vtol.petpal.domain.model.tasks.TaskUi
import com.vtol.petpal.domain.model.tasks.details.VetDetails
import com.vtol.petpal.domain.usecases.AppUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsViewModel @Inject constructor(
    private val appUseCases: AppUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])
    private val _range = MutableStateFlow(WeightRange.DAYS_7)

    private val _state = MutableStateFlow(DetailsState(isLoading = true))
    val state = _state.asStateFlow()


    init {
        observePetInfo()
    }

    fun addWeight(petId: String?, weightRecord: WeightRecord) {
        if (petId != null){

            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    appUseCases.addWeight(petId, weightRecord)
                    _state.update { it.copy(isLoading = false) }
                } catch (e: Exception) {
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            }
        }
    }


    fun observePetInfo() {

        viewModelScope.launch {
            try {

                val pet = appUseCases.getPet(petId)

                combine(
                    appUseCases.getTasksById(petId),
                    appUseCases.getWeights(petId),
                    _range
                ) { tasks, weights, range ->
                    val filtered = filterWeights(weights, range)

                    DetailsState(
                        pet = pet,
                        tasks = tasks.sortedBy { it.dateTime },
                        lastWeight = filtered,
                        range = range,
                        lastTask = tasks.filter { !it.isCompleted }.maxByOrNull { it.dateTime }
                    )
                }
                    .catch { e ->
                        _state.update {
                            it.copy(
                                isLoading = false, error = e.message
                            )
                        }
                    }
                    .collect { newState -> _state.value = newState }


            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false, error = e.message
                    )
                }
            }
        }
    }


    fun updateWeightFilter(range: WeightRange){
        _range.value = range
    }

//    fun getTask() {
//        viewModelScope.launch {
//
//            _state.value = DetailsState(isLoading = true)
//            try {
//                appUseCases.getTasksById(petId).collect { tasks ->
//                    _state.value = DetailsState(
//                        nextTask = tasks.sortedBy { it.dateTime }[0],
//                        isLoading = false
//                    )
//                }
//
//            } catch (e: Exception) {
//                _state.value = DetailsState(
//                    isLoading = false,
//                    error = e.message
//                )
//            }
//
//        }
//
//    }

}

fun filterWeights(
    entries: List<WeightRecord>,
    range: WeightRange
): List<WeightRecord> {

    if (range == WeightRange.ALL) return entries

    val now = System.currentTimeMillis()

    val rangeMillis = when (range) {
        WeightRange.DAYS_7 -> 7L * 24 * 60 * 60 * 1000
        WeightRange.DAYS_30 -> 30L * 24 * 60 * 60 * 1000
        WeightRange.MONTHS_6 -> 180L * 24 * 60 * 60 * 1000
        WeightRange.YEAR_1 -> 365L * 24 * 60 * 60 * 1000
        else -> Long.MAX_VALUE
    }

    return entries
        .filter { record ->
            val diff = now - record.timestamp
            diff in 0..rangeMillis   // 🔥 safer than <= only
        }
        .sortedByDescending { it.timestamp } // 🔥 important for UI correctness
}

data class DetailsState(
    val tasks: List<TaskUi> = emptyList(),
    val lastTask: TaskUi? = TaskUi(
        petId = "",
        title = "",
        note = "",
        type = TaskType.VET,
        dateTime = System.currentTimeMillis(),
        isCompleted = false,
        repeatInterval = null,
        details = VetDetails("", ""),
        id = 0,
        syncStatus = SyncStatus.SYNCED
    ),
    val pet: Pet? = Pet(),
    // This will work in both OverView and Health tabs, since it in overview will just take the last sorted one, which the last weight the user updated so it represent the current pet's weight :)
    val lastWeight: List<WeightRecord> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val range: WeightRange = WeightRange.DAYS_7
)