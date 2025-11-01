package com.vtol.petpal.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.domain.model.Vet

interface AppRepository {
    suspend fun getNearLocations(userLocation: LatLng, category: PlaceCategory): List<Vet>
}