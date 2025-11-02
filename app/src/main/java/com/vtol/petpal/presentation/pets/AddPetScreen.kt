package com.vtol.petpal.presentation.pets

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.formatDate
import java.time.LocalDate

@Composable
fun AddPetScreen() {
    val context = LocalContext.current
    var petName by remember { mutableStateOf("") }
    var petSpecie by remember { mutableStateOf("") }
    var petWeight by remember { mutableStateOf("") }
    var selectWUnit by remember { mutableStateOf(WeightUnit.KG) }
    var gender by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }


    var expanded by remember { mutableStateOf(false) }

    var birthDate by remember { mutableStateOf<LocalDate?>(null) }

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, day ->
            birthDate = LocalDate.of(year, month + 1, day)
        },
        LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth
    )

    val angle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "Angle Animation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Box(modifier= Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 14.dp)){
            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(42.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
            Text(modifier= Modifier.align(Alignment.Center),text = "Add Pet", style= MaterialTheme.typography.headlineMedium)
        }

        Column(
            modifier = Modifier
                .size(142.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .clickable { imagePickerLauncher.launch("image/*") }
        ) {

            if (imageUri == null) {
                Image(
                    modifier = Modifier.fillMaxSize().background(Color.LightGray),
                    painter = painterResource(R.drawable.ic_camera),
                    contentScale = ContentScale.Inside,
                    contentDescription = "Pet image"
                )
            } else {

                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Pet image"
                )

            }
        }
        Spacer(modifier= Modifier.height(4.dp))
        Text(text = "Add photo", color= Color.LightGray, style= MaterialTheme.typography.headlineSmall)


        Spacer(modifier = Modifier.height(16.dp))

        PetTextField(placeHolder = "Pet Name", value = petName) { petName = it }

        PetTextField(placeHolder = "Specie", value = petSpecie) { petSpecie = it }

        // TODO: Add a unit in the end of the text field using Row and drop down menu kg/pound/gram
        PetTextField(
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            placeHolder = "Weight(kg)",
            trailingIcon = Icons.Default.Face,
            value = petWeight,
            selectedUnit = selectWUnit,
            onTrailingClicked = {
                selectWUnit = WeightUnit.entries.iterator().next()
            }
        ) { petWeight = it }


        // Gender
        Box {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = gender,
                placeholder = { Text("Choose a gender") },
                onValueChange = { gender = it },
                shape = RoundedCornerShape(10.dp),
                leadingIcon = if (gender.isNotEmpty()) {
                    {
                        Image(
                            painter = painterResource(R.drawable.ic_male),
                            contentDescription = "gender icon"
                        )
                    }
                } else null,
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(0.5f)
                            .rotate(degrees = angle),
                        onClick = { expanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray),
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                PetGender.entries.forEach { genderName ->
                    DropdownMenuItem(
                        text = { Text(genderName.name) },
                        onClick = {
                            expanded = false
                            gender = genderName.name
                        }
                    )
                }
            }
        }
        // Birth date picker

        OutlinedTextField(
            value = birthDate?.formatDate() ?: "",
            onValueChange = {},
            label = { Text("Birth Date") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePicker.show() },
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

        // Add button: float button or regular button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightPurple)
        ) {
            Text(modifier= Modifier.padding(vertical = 2.dp),text = "Add Pet", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Preview
@Composable
fun MyScreenPets() {
    PetPalTheme {
        AddPetScreen()

    }
}