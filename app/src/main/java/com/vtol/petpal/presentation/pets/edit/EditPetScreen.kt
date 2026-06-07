package com.vtol.petpal.presentation.pets.edit

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.presentation.add_pet.components.PetChipButton
import com.vtol.petpal.presentation.add_pet.components.PetDateTextField
import com.vtol.petpal.presentation.common.components.ChipButton
import com.vtol.petpal.presentation.common.components.LoadingIndicator
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.Constants
import com.vtol.petpal.util.Constants.species
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun EditPetScreen(
    state: EditPetUiState,
    event: (EditPetEvent) -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Crop launcher — receives the cropped URI result
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            event(EditPetEvent.OnImageChanged(croppedUri))
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val destUri = Uri.fromFile(
                File(context.cacheDir, "cropped_pet_${System.currentTimeMillis()}.jpg")
            )
            val options = UCrop.Options().apply {
                setCircleDimmedLayer(true)
                setShowCropGrid(false)
                setShowCropFrame(false)
            }

            val cropIntent = UCrop.of(it, destUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512)
                .withOptions(options)
                .getIntent(context)

            cropLauncher.launch(cropIntent)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(petPalGradient)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .padding(bottom = 72.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppIconButton { navigateUp() }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Edit Pet",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = state.petName,
                            fontSize = 20.sp,
                            color = ExtraLightPurple
                        )

                    }

                    TextButton(
                        onClick = {
                            event(EditPetEvent.OnSaveClicked)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.White.copy(
                                alpha = 0.2f
                            ), contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }
                }
            }


            Box(
                modifier = Modifier
                    .offset(y = (-62).dp)
                    .padding(top = 16.dp, bottom = 4.dp)
                    .size(110.dp),
                contentAlignment = Alignment.Center
            ) {
                val imageModel = state.imageUri ?: state.imagePath
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(
                            width = if (state.imagePath.isEmpty()) 0.dp else 2.dp,
                            color = LightPurple,
                            shape = CircleShape
                        )
                        .background(LightPurple),
                    model = ImageRequest.Builder(context)
                        .data(imageModel)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.pet_placeholder),
                    error = painterResource(R.drawable.pet_placeholder),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Profile image"
                )

                // edit image button
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MainPurple),
                    shape = CircleShape,
                    border = BorderStroke(0.3.dp, Color.White)

                ) {
                    Icon(
                        modifier = Modifier
                            .clickable { imagePickerLauncher.launch("image/*") }
                            .padding(8.dp)
                            .size(12.dp),
                        painter = painterResource(R.drawable.ic_edit_v2),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                // delete icon
                if (state.imagePath.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopStart),
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        shape = CircleShape,
                    ) {
                        Icon(
                            modifier = Modifier
                                .clickable { event(EditPetEvent.OnRemoveClicked) }
                                .padding(6.dp)
                                .size(14.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .offset(y = (-32).dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    species.forEach { (icon, specie) ->
                        PetChipButton(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.specie == specie) MainPurple else Color.White,
                            ),
                            icon = icon,
                            txt = specie,
                            tint = if (state.specie == specie) Color.White else LightPurple
                        ) {
                            event(EditPetEvent.OnSpecieChanged(specie))
                        }
                    }
                }

                state.error?.let {
                    Text(text = it, fontSize = 13.sp, color = Color.Red)
                }
            }


            PetTextField(
                label = "Pet Name",
                leadingIcon = R.drawable.ic_person,
                placeHolder = "e.g. Buddy, Luna...",
                value = state.petName,
                error = state.petNameError
            ) { event(EditPetEvent.OnNameChanged(it)) }

            PetTextField(
                placeHolder = "Breed (optional)",
                leadingIcon = R.drawable.ic_mark,
                value = state.breed,
            ) { event(EditPetEvent.OnBreedChanged(it)) }

            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Gender",
                        fontSize = 14.sp,
                        color = LightPurple,
                        fontWeight = FontWeight.Medium
                    )


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Constants.genders.forEach { (icon, gender) ->
                            val isSelected = state.gender == gender
                            PetChipButton(
                                modifier = Modifier.weight(1f),
                                padding = 12.dp,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MainPurple else Color.White,
                                ),
                                icon = icon,
                                txt = gender.genderTxt,
                                tint = if (isSelected) Color.White else LightPurple
                            ) {
                                if (isSelected) {
                                    event(EditPetEvent.OnGenderChanged(PetGender.Unknown))

                                } else {
                                    event(EditPetEvent.OnGenderChanged(gender))

                                }
                            }
                        }
                    }
                }
            }


            PetDateTextField(
                date = state.birthDate
            ) {
                focusManager.clearFocus()
                event(EditPetEvent.OnBirthDateChanged(it))
            }

            Column {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Personality",
                    fontSize = 14.sp,
                    color = LightPurple,
                    fontWeight = FontWeight.Medium
                )

                val tags =
                    listOf("Playful", "Calm", "Energetic", "Shy", "Friendly")

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 4,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    tags.forEach { tag ->
                        val isSelected = tag in state.personality
                        ChipButton(
                            text = tag,
                            bgColor = Color.White,
                            textColor = if (isSelected) MainPurple else ExtraLightPurple,
                            borderColor = if (isSelected) MainPurple else ExtraLightPurple
                        ) {
                            event(EditPetEvent.OnPersonalityChanged(tag))
                        }
                    }
                }
            }

            // 'Add button': float button or regular button
            SaveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                text = "Save Pet",
                icon = Icons.Default.Favorite,
                isLoading = state.isLoading,
                color = LightPurple
            ) {
                focusManager.clearFocus()
                event(EditPetEvent.OnSaveClicked)
            }
        }
    }

    if (state.isLoading) {
        LoadingIndicator()
    }

}


@Preview
@Composable
fun EditPetPreview() {
    PetPalTheme {
        EditPetScreen(
            state = EditPetUiState(),
            event = {},
            navigateUp = {}
        )

    }
}