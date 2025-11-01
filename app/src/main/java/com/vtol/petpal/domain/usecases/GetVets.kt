package com.vtol.petpal.domain.usecases

import com.google.android.gms.maps.model.LatLng
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.domain.model.Vet
import com.vtol.petpal.domain.repository.AppRepository

class GetVets(
    private val repository: AppRepository,
) {
    suspend operator fun invoke(userLocation: LatLng, category: PlaceCategory): List<Vet> {
        return repository.getNearLocations(userLocation,category)
    }
}