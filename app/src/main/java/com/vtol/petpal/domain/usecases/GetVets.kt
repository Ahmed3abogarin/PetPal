package com.vtol.petpal.domain.usecases

import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.model.map.PlaceCategory
import com.vtol.petpal.domain.model.map.PlaceAddress
import com.vtol.petpal.domain.repository.MapsRepository

class GetVets(
    private val repository: MapsRepository,
) {
    suspend operator fun invoke(userLocation: LatLng, category: PlaceCategory): List<PlaceAddress> {
        return repository.getNearLocations(userLocation,category)
    }
}