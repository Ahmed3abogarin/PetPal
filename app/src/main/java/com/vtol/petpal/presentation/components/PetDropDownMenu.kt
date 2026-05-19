package com.vtol.petpal.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.cardColors
import com.vtol.petpal.util.rememberShimmerBrush

@Composable
fun PetDropDownMenu(
    petsList: List<Pet>,
    isLoading: Boolean,
    onConfirm: (Pet) -> Unit,
    selectedPet: Pet?
) {

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }


    // the text input field
    Card(
        onClick = { expanded = true },
        border = BorderStroke(width = 1.dp, color = LightPurple),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {

        if (isLoading) {
            val brush = rememberShimmerBrush(cardColors)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                        .background(brush)

                )

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(width = 120.dp, height = 10.dp)
                        .background(brush)
                )

            }

            return@Card
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current).data(selectedPet?.imagePath)
                        .build(),
                    error = painterResource(R.drawable.pet_placeholder),
                    placeholder = painterResource(R.drawable.pet_placeholder),
                    contentScale = ContentScale.Crop,
                    contentDescription = "pet's image"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = selectedPet?.petName?.ifBlank { "Select Pet" } ?: "Select Pet")
            }

            val icon =
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
            Icon(icon, null, tint = MainPurple)

        }
    }

    // Pop up dialog
    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false }
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Select pet")
                    IconButton(
                        onClick = {
//                        onConfirm(selectedPetId)
                            expanded = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            tint = MainPurple,
                            contentDescription = ""
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 4.dp)
                )

                petsList.forEachIndexed { index, pet ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clickable {
                                onConfirm(pet)
                                expanded = false
                            }
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                contentDescription = "",
                                model = ImageRequest.Builder(context).data(pet.imagePath).build(),
                                placeholder = painterResource(R.drawable.pet_placeholder),
                                error = painterResource(R.drawable.pet_placeholder),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = pet.petName)

                        }
                        RadioButton(
                            selected = selectedPet?.id == pet.id,
                            onClick = null// handled by row click
                        )
                    }

                    // Add a divider after each item except for the last one
                    if (index < petsList.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .alpha(0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        PetDropDownMenu(
            petsList = listOf(Pet()),
            selectedPet = Pet(),
            onConfirm = {},
            isLoading = true
        )
    }
}