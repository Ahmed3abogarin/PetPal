package com.vtol.petpal.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.model.Vet

interface AppRepository {
    suspend fun getNearByVets(userLocation: LatLng): List<Vet>
}