package com.vtol.petpal.presentation.explore.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.vtol.petpal.domain.model.map.PlaceAddress
import androidx.core.net.toUri

object MapsIntentHelper {

    fun openGoogleMaps(context: Context, place: PlaceAddress) {
        val uri = "geo:0,0?q=${place.lat},${place.lng}(${Uri.encode(place.name)})".toUri()

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                "https://www.google.com/maps/search/?api=1&query=${place.lat},${place.lng}".toUri()
            )
            context.startActivity(webIntent)
        }
    }
}