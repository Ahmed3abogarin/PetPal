package com.vtol.petpal.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

// utils/GalleryMediaManager.kt
class GalleryMediaManager {

    private val context: Context

    constructor(context: Context) {
        this.context = context
    }

    // creates a temp file URI for camera capture
    fun createImageUri(): Uri {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "photo_${System.currentTimeMillis()}.jpg"
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}