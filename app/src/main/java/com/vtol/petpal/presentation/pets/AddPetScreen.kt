package com.vtol.petpal.presentation.pets

import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.home.components.DatePickerModal
import com.vtol.petpal.presentation.home.components.MyDropDownMenu
import com.vtol.petpal.presentation.nearby.components.LoadingIndicator
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.util.Resource
import com.vtol.petpal.util.formatDate

@Composable
fun AddPetScreen(viewModel: PetViewModel, navigateUp: () -> Unit) {
    val context = LocalContext.current
    var petName by remember { mutableStateOf("") }
    var petNameError by remember { mutableStateOf<String?>(null) }

    var petBreed by remember { mutableStateOf("") }
    var petBreedError by remember { mutableStateOf<String?>(null) }


    var petWeight by remember { mutableStateOf("") }
    var petWeightError by remember { mutableStateOf<String?>(null) }


    var selectWUnit by remember { mutableStateOf(WeightUnit.KG) }

    var gender by remember { mutableStateOf(PetGender.Unknown) }
    var genderError by remember {mutableStateOf<String?>(null)}
    var selectedIndex by remember { mutableIntStateOf(-1) }

    var birthDate by remember { mutableStateOf<Long?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    // to loss the focus
    val focusManger = LocalFocusManager.current

    // to disable the ui when it is loading
    var isUiEnabled by remember { mutableStateOf(true) }

    val state by viewModel.addPetState.collectAsState()
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
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        modifier = Modifier.size(42.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
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

                if (imageUri == null) {
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
                        painter = rememberAsyncImagePainter(imageUri),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "Pet image"
                    )

                }
            }



            Spacer(modifier = Modifier.height(16.dp))


            PetTextField(placeHolder = "Pet Name", value = petName, error = petNameError) { petName = it }

            PetTextField(placeHolder = "Specie", value = petBreed, error = petBreedError) { petBreed = it }

            // TODO: Add a unit in the end of the text field using Row and drop down menu kg/pound/gram
            PetTextField(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                placeHolder = "Weight(kg)",
                trailingIcon = Icons.Default.Face,
                value = petWeight,
                selectedUnit = selectWUnit,
                error = petWeightError,
                onTrailingClicked = {
                    val nextIndex =
                        (WeightUnit.entries.indexOf(selectWUnit) + 1) % WeightUnit.entries.size
                    selectWUnit = WeightUnit.entries[nextIndex]
                }
            ) { petWeight = it }


            // Gender
            MyDropDownMenu(
                items = PetGender.entries.map { it },
                selectedIndex = selectedIndex,
                onItemSelected = { index, selectedGender ->
                    focusManger.clearFocus()
                    selectedIndex = index
                    gender = selectedGender
                },
                error = genderError,
                label = "Gender"
            )


            // Birth date picker
            OutlinedTextField(
                value = birthDate?.formatDate() ?: "",
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
                    onDateSelected = { birthDate = it },
                    onDismiss = { showDatePicker = false }
                )

            }


            // 'Add button': float button or regular button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                enabled = isUiEnabled,
                onClick = {
                    focusManger.clearFocus()

                    // validate the inputs
                    var isValid = true

                    petNameError = if (petName.isBlank()) {
                        isValid = false
                        "This field cannot be empty"
                    } else null

                    petBreedError = if (petBreed.isBlank()) {
                        isValid = false
                        "This field cannot be empty"
                    } else null

                    petWeightError = if (petWeight.isBlank()) {
                        isValid = false
                        "This field cannot be empty"
                    } else null

                    genderError = if (genderError == null) {
                        isValid = false
                        "This field cannot be empty"
                    } else null

                    if (isValid){
                        viewModel.addPet(
                            Pet(
                                petName = petName,
                                birthDate = birthDate,
                                gender = gender,
                                breed = petBreed,
                                weightUnit = selectWUnit
                            )
                        )
                    }



                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightPurple)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 2.dp),
                    text = "Add Pet",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

            when (state) {
                is Resource.Loading -> {
                    Log.e("TTTOLSDF", "its loading now")
                    isUiEnabled = false
                    LoadingIndicator()
                }

                is Resource.Success -> {
                    Log.e("TTTOLSDF", "its succeed")
                    navigateUp()
                    Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()

                }

                is Resource.Error -> {
                    val error = (state as Resource.Error).message
                    Log.e("TTTOLSDF", error)
                }

                else -> Unit
            }

    }
}

//@Preview
//@Composable
//fun MyScreenPets() {
//    PetPalTheme {
//        AddPetScreen(navigateUp = {})
//
//    }
//}