package com.vtol.petpal.domain.repository

import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.domain.model.VetAddress

interface MapsRepository {
    suspend fun getNearLocations(userLocation: LatLng, category: PlaceCategory): List<VetAddress>
}