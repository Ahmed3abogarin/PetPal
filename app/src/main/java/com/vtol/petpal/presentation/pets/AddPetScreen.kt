package com.vtol.petpal.presentation.pets

import android.app.DatePickerDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.domain.model.PetGender
import com.vtol.petpal.domain.model.WeightUnit
import com.vtol.petpal.presentation.home.components.CustomDropdownMenu
import com.vtol.petpal.presentation.home.components.MyDropDownMenu
import com.vtol.petpal.presentation.nearby.components.LoadingIndicator
import com.vtol.petpal.presentation.pets.components.PetTextField
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.util.Resource
import com.vtol.petpal.util.formatDate
import java.util.Calendar

@Composable
fun AddPetScreen(viewModel: PetViewModel, navigateUp: () -> Unit) {
    val context = LocalContext.current
    var petName by remember { mutableStateOf("") }
    var petSpecie by remember { mutableStateOf("") }
    var petWeight by remember { mutableStateOf("") }
    var selectWUnit by remember { mutableStateOf(WeightUnit.KG) }
    var gender by remember { mutableStateOf(PetGender.Unknown) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }


    var expanded by remember { mutableStateOf(false) }

    var birthDate by remember { mutableStateOf<Timestamp?>(null) }

    // TODO : what is the difference between the old and the new one
//    val datePicker = DatePickerDialog(
//        context,
//        { _, year, month, day ->
//            birthDate = LocalDate.of(year, month + 1, day)
//        },
//        LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth
//    )

    val calendar = Calendar.getInstance()

    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            val date = calendar.time
            val timestamp = Timestamp(date)
            birthDate = timestamp
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
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
            IconButton(onClick = { navigateUp() }) {
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
                    contentScale = ContentScale.FillBounds,
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
//        CustomDropdownMenu(
//            selectedOption = "",
//            onOptionSelected = {},
//            options = PetGender.entries.map { it.name }
//        )

        var selectedIndex by remember { mutableIntStateOf(-1) }

        MyDropDownMenu(
            items = PetGender.entries.map { it.name },
            selectedIndex = selectedIndex,
            onItemSelected = { index, _ -> selectedIndex = index},
            label = "Gender"
        )


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
      val state by viewModel.state.collectAsState()
        when(state){
            is Resource.Loading -> {
                LoadingIndicator()
            }
            is Resource.Success -> {
                Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()

            }
            is Resource.Error -> {
                val error = (state as Resource.Error).message
                Log.e("sgsdhsd", error)
            }
            else -> Unit
        }

        // Add button: float button or regular button
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            onClick = {
                    viewModel.addPet(
                        Pet(
                            petName = petName,
                            birthDate = birthDate,
                            gender = gender
                        )
                    )

            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightPurple)
        ) {
            Text(modifier= Modifier.padding(vertical = 2.dp),text = "Add Pet", style = MaterialTheme.typography.headlineSmall)
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