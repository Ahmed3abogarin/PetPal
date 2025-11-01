package com.vtol.petpal.data.repository

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import com.google.android.libraries.places.api.net.SearchNearbyResponse
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.domain.model.Vet
import com.vtol.petpal.domain.repository.AppRepository
import kotlinx.coroutines.tasks.await

class AppRepositoryImpl(
     context: Context,
) : AppRepository {
    private val placesClient: PlacesClient by lazy {
        if (!Places.isInitialized()) {
            Places.initialize(context.applicationContext, "AIzaSyAoJgOIASf44vRfmN1UXfSBmGWrtqk_Qt4")
        }
        Places.createClient(context.applicationContext)
    }

    override suspend fun getNearLocations(userLocation: LatLng,category: PlaceCategory): List<Vet> {
        // Define what fields we want back
        val fields = listOf(
            Place.Field.DISPLAY_NAME,
            Place.Field.LOCATION,
            Place.Field.FORMATTED_ADDRESS,
            Place.Field.TYPES
        )

        // Define the search area — e.g., 5000 m radius around userLocation
        val bounds = CircularBounds.newInstance(userLocation, 5000.0)

        // Build the SearchNearbyRequest
        val request = SearchNearbyRequest
            .builder(bounds, fields)
            .setIncludedTypes(listOf(category.apiType))
            .setMaxResultCount(20)
            .setRankPreference(SearchNearbyRequest.RankPreference.POPULARITY)
            .build()
        // Execute the request
        val response: SearchNearbyResponse = placesClient.searchNearby(request).await()

        // Map the result to domain model
        return response.places.mapNotNull { place ->
            place.location?.let { latLng ->
                Vet(
                    name = place.displayName ?: "Vet",
                    lat = latLng.latitude,
                    lng = latLng.longitude,
                    address = place.formattedAddress
                )
            }
        }
    }
}
