package com.vtol.petpal.domain

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LatLng? {
        // Try last known location first
        val lastLocation = fusedLocationClient.lastLocation.await()
        if (lastLocation != null) return LatLng(lastLocation.latitude, lastLocation.longitude)

        // Fallback: request a fresh location
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(0) // force fresh location
            .setDurationMillis(10000) // wait up to 3 seconds
            .build()

        val freshLocation = fusedLocationClient.getCurrentLocation(request, null).await()
        return freshLocation?.let { LatLng(it.latitude, it.longitude) }
    }
}