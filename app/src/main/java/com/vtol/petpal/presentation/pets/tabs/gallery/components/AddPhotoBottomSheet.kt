package com.vtol.petpal.presentation.pets.tabs.gallery.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.MainPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhotoBottomSheet(
    onDismiss: () -> Unit,
    onCamera: () -> Unit,
    onGallery: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Add Photo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ListItem(
                headlineContent = { Text("Take a photo") },
                leadingContent = {
                    Icon(Icons.Default.PhotoCamera, null, tint = MainPurple)
                },
                modifier = Modifier.clickable { onCamera() }
            )
            ListItem(
                headlineContent = { Text("Choose from gallery") },
                leadingContent = {
                    Icon(Icons.Default.Photo, null, tint = MainPurple)
                },
                modifier = Modifier.clickable { onGallery() }
            )
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}