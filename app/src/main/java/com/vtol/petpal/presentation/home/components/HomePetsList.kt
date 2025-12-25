package com.vtol.petpal.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun HomePetsList(modifier: Modifier = Modifier, pets: List<Pet>, onAddPetClicked: () -> Unit) {
    val context = LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(pets) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    model = ImageRequest.Builder(context).data(it.imagePath).build(),
                    placeholder = painterResource(R.drawable.cat),
                    error = painterResource(R.drawable.cat),
                    contentDescription = "pet image",
                    contentScale = ContentScale.Crop
                )
                Text(text = it.petName, fontSize = 12.sp)
            }
        }
        item {
            Box(
                modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MainPurple), contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onAddPetClicked() }) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
    }
}