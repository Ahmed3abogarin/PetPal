package com.vtol.petpal.presentation.nearby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.LocationProvider
import com.vtol.petpal.domain.model.Vet
import com.vtol.petpal.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NearViewModel @Inject constructor(
    private val repository: AppRepository,
    private val locationProvider: LocationProvider
): ViewModel(){

    private val _vets = MutableStateFlow<List<Vet>>(emptyList())
    val vets = _vets.asStateFlow()

    private val _location = MutableStateFlow<LatLng?>(null)
    val location = _location.asStateFlow()

    init {
        getVets()
    }

    fun getVets(){
        viewModelScope.launch {
            val userLocation = locationProvider.getCurrentLocation()
//            val jeddahLocation = LatLng(21.4858, 39.1925)
            userLocation?.let {
                Log.v("Current",it.toString())
                _location.value = it
                _vets.value = repository.getNearByVets(it)
            }

        }
    }
}
