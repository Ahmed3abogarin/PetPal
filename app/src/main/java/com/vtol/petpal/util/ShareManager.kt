package com.vtol.petpal.util

import android.content.Context
import android.content.Intent

class ShareManager(
    private val context: Context
) {
    fun shareApp() {
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
}