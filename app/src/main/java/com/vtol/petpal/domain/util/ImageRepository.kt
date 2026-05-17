package com.vtol.petpal.domain.util

import android.net.Uri

interface ImageCompressor {
    suspend fun compress(
        uri: Uri,
        maxWidthPx: Int = 512,
        quality: Int = 75
    ): ByteArray
}
