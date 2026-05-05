package com.vtol.petpal.domain.model.map

import android.net.Uri
import com.google.android.libraries.places.api.model.OpeningHours

data class PlaceAddress(
    val id: String,
    val name: String,
    val url: Uri?,
    val photo: Uri?,
    val lat: Double,
    val lng: Double,
    val rating: Double?,
    val totalRating: Int?,
    val openingHours: OpeningHours?,
    val phoneNumber: String?,
    val isOpen: Boolean?,
    val distance: Float?,
    val openingStatus: OpeningStatus
)

data class OpeningStatus(
    val closingTime: String? = null,
    val is24Hours: Boolean = false,
    val nextOpeningTime: String? = null,
    val nextOpeningDay: String? = null  // "Tomorrow", "Wednesday", etc.
)