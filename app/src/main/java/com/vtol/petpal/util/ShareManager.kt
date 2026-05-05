package com.vtol.petpal.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object ShareManager {
    fun shareApp(context: Context) {
        val packageName = context.packageName

        val text = """
            Join me on PetPal 🐾
            
            Manage your pets easily.
            
            https://play.google.com/store/apps/details?id=$packageName
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        context.startActivity(
            Intent.createChooser(intent, "Invite Friends")
        )
    }

    // Inside your UI layer (e.g., an Activity or Composable)
    fun openWebsite(context: Context, url: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url
        }
        context.startActivity(intent)
    }

    fun openDialer(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }
}