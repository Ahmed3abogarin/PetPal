package com.vtol.petpal.presentation.pets.tabs.gallery.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.domain.model.PetPhoto

@Composable
fun PhotoGridItem(photo: PetPhoto, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

//        // show pending indicator
//        if (photo.syncStatus == SyncStatus.PENDING || photo.syncStatus == SyncStatus.FAILED) {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.TopEnd)
//                    .padding(4.dp)
//                    .size(8.dp)
//                    .background(
//                        color = if (photo.syncStatus == SyncStatus.FAILED)
//                            Color.Red else Color.Yellow,
//                        shape = CircleShape
//                    )
//            )
//        }
    }
}