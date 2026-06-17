package com.vtol.petpal.presentation.pets.tabs.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.pets.tabs.gallery.components.AddPhotoBottomSheet
import com.vtol.petpal.presentation.pets.tabs.gallery.components.PhotoGridItem
import com.vtol.petpal.presentation.pets.tabs.gallery.components.PhotoViewer
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.GalleryMediaManager
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun GalleryContentScreen(state: GalleryUiState, event: (GalleryEvent) -> Unit) {
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val mediaManager = remember { GalleryMediaManager(context) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraUri?.let { event(GalleryEvent.PhotoPicked(it)) }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { event(GalleryEvent.PhotoPicked(it)) }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (state.isUploading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                color = MainPurple
            )
        }

        if (state.photos.isEmpty() && !state.isLoading) {
            // ── Empty state ──────────────────────────────────
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(MainPurple.copy(alpha = 0.1f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_gallery),
                        contentDescription = null,
                        tint = MainPurple,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "No photos yet",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Capture your favourite moments",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        } else {
            // ── Photo grid ───────────────────────────────────
            val grouped = state.photos.groupBy { photo ->
                val date = Instant.ofEpochMilli(photo.createdAt)
                    .atZone(ZoneId.systemDefault()).toLocalDate()
                date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .uppercase() + " " + date.year
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                grouped.forEach { (month, photos) ->
                    item(span = { GridItemSpan(3) }) {
                        Text(
                            text = month,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                    items(photos, key = { it.id }) { photo ->
                        PhotoGridItem(
                            photo = photo,
                            onClick = { event(GalleryEvent.SelectPhoto(photo)) }
                        )
                    }
                }
            }
        }

        // ── FAB ──────────────────────────────────────────────
        FloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .navigationBarsPadding(),
            containerColor = MainPurple,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add photo", tint = Color.White)
        }
    }

    // ── Full screen viewer ───────────────────────────────────
    state.selectedPhoto?.let { photo ->
        PhotoViewer(
            photo = photo,
            onDismiss = { event(GalleryEvent.SelectPhoto(null))},
            onDelete = {
                event(GalleryEvent.DeletePhoto(photo))
                event(GalleryEvent.SelectPhoto(null))
            }
        )
    }

    // ── Bottom sheet ─────────────────────────────────────────
    if (showBottomSheet) {
        AddPhotoBottomSheet(
            onDismiss = { showBottomSheet = false },
            onCamera = {
                showBottomSheet = false
                val uri = mediaManager.createImageUri()
                cameraUri = uri
                cameraLauncher.launch(uri)
            },
            onGallery = {
                showBottomSheet = false
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}

@Preview
@Composable
fun GalleryPreviewContent() {
    PetPalTheme {
        GalleryContentScreen(
            state = GalleryUiState()
        ) { }
    }
}