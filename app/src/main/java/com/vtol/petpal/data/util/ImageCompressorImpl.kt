package com.vtol.petpal.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import com.vtol.petpal.domain.util.ImageCompressor
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageCompressorImpl @Inject constructor(
    private val context: Context
) : ImageCompressor {

    override suspend fun compress(
        uri: Uri,
        maxWidthPx: Int,
        quality: Int
    ): ByteArray = withContext(Dispatchers.IO) {

        var sourceFile: File? = null
        var compressedFile: File? = null

        try {
            sourceFile = uriToFile(uri)

            // FIX 1: Use standard WEBP for all versions to prevent Zelory's infinite loop bug
            @Suppress("DEPRECATION")
            val format = Bitmap.CompressFormat.WEBP

            compressedFile = Compressor.compress(
                context,
                sourceFile
            ) {
                // Zelory's resolution automatically respects aspect ratios,
                // so simply passing the max boundaries is enough.
                resolution(maxWidthPx, maxWidthPx)
                quality(quality)
                format(format)
            }

            // Return the bytes
            compressedFile.readBytes()

        } finally {
            // Clean up temporary files
            sourceFile?.delete()

            if (compressedFile != null &&
                compressedFile.exists() &&
                compressedFile.absolutePath != sourceFile?.absolutePath
            ) {
                compressedFile.delete()
            }
        }
    }

    // Helper to stream secure Uri bytes into a temporary sandbox File
    private fun uriToFile(uri: Uri): File {
        val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)

        // FIX 2: Added `.use` to the InputStream to prevent file descriptor leaks
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        } ?: throw Exception("Cannot open input stream")

        return file
    }
}