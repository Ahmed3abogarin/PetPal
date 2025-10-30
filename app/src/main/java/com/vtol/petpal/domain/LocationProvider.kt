package com.vtol.petpal.domain

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await

class LocationProvider (context: Context){
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LatLng? {
        val location = fusedLocationClient.lastLocation.await()
        return location?.let { LatLng(it.latitude, it.longitude) }
    }
}