package com.vtol.petpal.presentation.nearby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.domain.model.VetAddress
import com.vtol.petpal.domain.usecases.MapsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NearViewModel @Inject constructor(
    private val appUseCases: MapsUseCases,
    private val locationProvider: LocationProvider
): ViewModel(){

    private val _locations = MutableStateFlow<List<VetAddress>>(emptyList())
    val locations = _locations.asStateFlow()

    private val _selectedCategory = MutableStateFlow(PlaceCategory.VETS)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _location = MutableStateFlow<LatLng?>(null)
    val location = _location.asStateFlow()

    init {
        getLocations()
    }

    fun onCategorySelected(category: PlaceCategory) {
        _selectedCategory.value = category
        getLocations()
    }

    fun getLocations(){
        viewModelScope.launch {
            val userLocation = locationProvider.getCurrentLocation()
//            val jeddahLocation = LatLng(21.4858, 39.1925)
            userLocation?.let {
                Log.v("Current",it.toString())
                _location.value = it
                _locations.value = appUseCases.getNearLocations(it,selectedCategory.value)
            }

        }
    }
}
