package com.vtol.petpal.presentation.add_pet

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.components.BackArrow
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.home.components.DatePickerModal
import com.vtol.petpal.presentation.home.components.MyDropDownMenu
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.util.formatDate

@Composable
fun AddPetScreen(
    state: AddPetState,
    event: (AddPetEvent) -> Unit,
    navigateUp: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var showDatePicker by remember { mutableStateOf(false) }


    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> event(AddPetEvent.OnImageChanged(uri)) }

    // to loss the focus
    val focusManger = LocalFocusManager.current


    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 14.dp)
            ) {
                BackArrow { navigateUp() }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Add Pet",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Column(
                modifier = Modifier
                    .size(142.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .clickable {
                        focusManger.clearFocus()
                        imagePickerLauncher.launch("image/*")
                    }
            ) {

                if (state.petImage == null) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray),
                        painter = painterResource(R.drawable.ic_camera),
                        contentScale = ContentScale.Inside,
                        contentDescription = "Pet image"
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add photo",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.headlineSmall
                    )
                } else {

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter(state.petImage),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Pet image"
                    )

                }
            }



            Spacer(modifier = Modifier.height(16.dp))


            PetTextField(
                placeHolder = "Pet Name",
                value = state.petName,
                error = state.petNameError
            ) { event(AddPetEvent.OnNameChanged(it)) }

            PetTextField(
                placeHolder = "Specie",
                value = state.petBreed,
                error = state.petBreedError
            ) { event(AddPetEvent.OnBreedChanged(it)) }

            // TODO: Add a unit in the end of the text field using Row and drop down menu kg/pound/gram
            PetTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeHolder = "Weight(kg)",
                trailingIcon = Icons.Default.Face,
                value = state.petWeight,
                selectedUnit = state.petWeightUnit,
                error = state.petWeightError,
                onTrailingClicked = {
                    val nextIndex =
                        (WeightUnit.entries.indexOf(state.petWeightUnit) + 1) % WeightUnit.entries.size
                    event(AddPetEvent.OnWeightUnitChanged(WeightUnit.entries[nextIndex]))
                }
            ) { event(AddPetEvent.OnWeightChanged(it)) }


            // Gender
            MyDropDownMenu(
                items = PetGender.entries.map { it },
                selectedIndex = selectedIndex,
                onItemSelected = { index, selectedGender ->
                    selectedIndex = index
                    event(AddPetEvent.OnGenderChanged(selectedGender))
                },
                error = state.petGenderError,
                label = "Gender"
            )


            // Birth date picker
            OutlinedTextField(
                value = state.petBirthDate?.formatDate() ?: "",
                colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black),
                onValueChange = {},
                label = { Text("Birth Date") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        focusManger.clearFocus()
                        showDatePicker = true
                    },
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            )

            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { event(AddPetEvent.OnBirthDateChanged(it)) },
                    onDismiss = { showDatePicker = false }
                )

            }
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) {} // Consume clicks so they don't hit fields below
                        .background(Color.Black.copy(alpha = 0.1f)), // Subtle dimming
                )
            }


            // 'Add button': float button or regular button
            SaveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                text = "Save Pet",
                isLoading = state.isLoading,
                color = LightPurple
            ) {
                focusManger.clearFocus()
                event(AddPetEvent.OnSaveClicked)
            }
        }
    }
}