package com.vtol.petpal.presentation.add_pet

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.add_pet.components.PetChipButton
import com.vtol.petpal.presentation.add_pet.components.PetDateTextField
import com.vtol.petpal.presentation.common.components.ChipButton
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient
import com.vtol.petpal.util.Constants.genders
import com.vtol.petpal.util.Constants.species
import com.yalantis.ucrop.UCrop
import java.io.File

@Composable
fun AddPetScreen(
    state: AddPetState,
    event: (AddPetEvent) -> Unit,
    navigateUp: () -> Unit
) {

    val context = LocalContext.current

    val gradient = listOf(LightPurple, ExtraLightPurple)



    // Crop launcher — receives the cropped URI result
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            event(AddPetEvent.OnImageChanged(croppedUri))
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

    val pickImage = { imagePickerLauncher.launch("image/*") }


    // to loss the focus
    val focusManager = LocalFocusManager.current

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
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppIconButton(modifier = Modifier.padding(start = 16.dp)) { navigateUp() }
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Add Pet",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            if (state.petImage == null) {

                Box(
                    modifier = Modifier
                        .offset(y = (-62).dp)
                        .size(124.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Brush.radialGradient(gradient))
                            .fillMaxSize()
                            .border(width = 3.dp, color = Color.White, shape = CircleShape)
                            .clickable { pickImage() }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center),
                            painter = painterResource(R.drawable.ic_camera),
                            contentDescription = null,
                            tint = MainPurple
                        )

                    }
                    Image(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(30.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape),
                        painter = painterResource(R.drawable.ic_profile_add),
                        contentScale = ContentScale.Crop,
                        contentDescription = "profile image"
                    )
                }


            } else {
                AsyncImage(
                    modifier = Modifier
                        .offset(y = (-62).dp)
                        .clip(CircleShape)
                        .size(124.dp)
                        .background(LightPurple)
                        .clickable { pickImage() },
                    model = ImageRequest.Builder(context).data(state.petImage).build(),
                    error = painterResource(R.drawable.ic_camera),
                    placeholder = painterResource(R.drawable.ic_camera),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Pet image"
                )
            }
        }

        Text(
            modifier = Modifier.offset(y = (-48).dp),
            text = "Tap to add a photo",
            fontSize = 14.sp,
            color = LightPurple
        )

        Column(
            modifier = Modifier
                .offset(y = (-24).dp)
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
                                containerColor = if (state.petSpecie == specie) MainPurple else Color.White,
                            ),
                            icon = icon,
                            txt = specie,
                            tint = if (state.petSpecie == specie) Color.White else LightPurple
                        ) {
                            event(AddPetEvent.OnSpecieChanged(specie))
                        }
                    }
                }

                state.petSpecieError?.let {
                    Text(text = it, fontSize = 13.sp, color = Color.Red)
                }
            }


            PetTextField(
                label = "Pet Name",
                leadingIcon = R.drawable.ic_person,
                placeHolder = "e.g. Buddy, Luna...",
                value = state.petName,
                error = state.petNameError
            ) { event(AddPetEvent.OnNameChanged(it)) }

            PetTextField(
                placeHolder = "Breed (optional)",
                leadingIcon = R.drawable.ic_mark ,
                value = state.petBreed,
            ) { event(AddPetEvent.OnBreedChanged(it)) }


            Row(
                modifier = Modifier.wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PetTextField(
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeHolder = "0.0",
                    label = "Weight",
                    value = state.petWeight,
                    trailingIcon = R.drawable.ic_edit,
                    text = state.petWeightUnit.displayName,
                    error = state.petWeightError,
                    onTrailingClicked = {
                        val nextIndex =
                            (WeightUnit.entries.indexOf(state.petWeightUnit) + 1) % WeightUnit.entries.size
                        event(AddPetEvent.OnWeightUnitChanged(WeightUnit.entries[nextIndex]))
                    }
                ) { event(AddPetEvent.OnWeightChanged(it)) }

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
                        genders.forEach { (icon, gender) ->
                            val isSelected = state.petGender == gender
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
                                    event(AddPetEvent.OnGenderChanged(PetGender.Unknown))

                                } else {
                                    event(AddPetEvent.OnGenderChanged(gender))

                                }
                            }
                        }
                    }
                }
            }


            PetDateTextField(
                date = state.petBirthDate
            ) {
                focusManager.clearFocus()
                event(AddPetEvent.OnBirthDateChanged(it))
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
                        val isSelected = tag in state.petPersonalities
                        ChipButton(
                            text = tag,
                            bgColor = Color.White,
                            textColor = if (isSelected) MainPurple else ExtraLightPurple,
                            borderColor = if (isSelected) MainPurple else ExtraLightPurple
                        ) {
                            event(AddPetEvent.OnPersonalityChanged(tag))
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
                color = MainPurple
            ) {
                focusManager.clearFocus()
                event(AddPetEvent.OnSaveClicked)
            }
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = false) {} // Consume clicks so they don't hit fields below
                .background(Color.Black.copy(alpha = 0.1f)), // Subtle dimming
        )
    }
}


@Preview
@Composable
fun AddPreview() {
    PetPalTheme {
        AddPetScreen(
            state = AddPetState(),
            event = {}
        ) { }
    }
}