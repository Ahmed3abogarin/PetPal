package com.vtol.petpal.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.model.map.PlaceCategory
import com.vtol.petpal.domain.model.map.PlaceAddress
import com.vtol.petpal.domain.usecases.MapsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val appUseCases: MapsUseCases,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val cache = mutableMapOf<PlaceCategory, List<PlaceAddress>>()


    init {
        getLocations()
    }

    fun onCategorySelected(category: PlaceCategory) {
        if (category != uiState.value.category) {
            _uiState.update {
                it.copy(category = category)
            }
            getLocations()
        }
    }

    fun getLocations() {
        viewModelScope.launch {
            val category = uiState.value.category

            // Return cached result immediately
            cache[category]?.let { cached ->
                _uiState.update { it.copy(locations = cached, isLoading = false) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }
            try {
                val userLocation = locationProvider.getCurrentLocation()
                if (userLocation == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Could not get location") }
                    return@launch
                }
                _uiState.update { it.copy(location = userLocation) }
                val locations = appUseCases.getNearLocations(userLocation, category)

                cache[category] = locations // Store in cache

                _uiState.update { it.copy(locations = locations, isLoading = false) }

            } catch (e: Exception) {
                Timber.tag("GoogleMapsApi").e(e.message.toString())
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}

data class UiState(
    val location: LatLng? = null,
    val locations: List<PlaceAddress> = emptyList(),
    val category: PlaceCategory = PlaceCategory.VETS,
    val isLoading: Boolean = false,
    val error: String? = null
)