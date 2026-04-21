package com.vtol.petpal.presentation.update

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.vtol.petpal.BuildConfig

@Composable
fun FlexibleUpdateScreen(onDismiss: () -> Unit) {
    val context = LocalContext.current
    // use local preferences to check if there is a flexible update available then retrieve it in the home screen
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("There is a new version of this app, including a new features and improvements")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(modifier = Modifier.weight(1f), onClick = {
                onDismiss()
            }) {
                Text(text = "Later")
            }
            Button(modifier = Modifier.weight(1f), onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".toUri()
                )
                context.startActivity(intent)
            }) {
                Text(text = "Update")
            }
        }
    }
}