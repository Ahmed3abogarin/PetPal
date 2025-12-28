package com.vtol.petpal.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun PetDropDownMenu(
    petsList: List<Pet>,
    onConfirm: (Pet) -> Unit,
    selectedPet: Pet
) {
//    val selectedPets = remember { mutableStateListOf(petsList[0]) }


    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        // the text input field
        OutlinedTextField(
            value = if (petsList.isNotEmpty()) selectedPet.petName else "Select Pet",
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(),
//        supportingText = {
//            if (error != null){
//                Text(error)
//            }
//        },
            trailingIcon = {
                val icon =
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                Icon(icon, "")
            },
            leadingIcon = {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.drawable.cat),
                    contentDescription = ""
                )
            },
            onValueChange = { },
            readOnly = true,
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { expanded = true },
            color = Color.Transparent,
        ) {}
    }


    // Pop up dialog
    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false }
        ) {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White).padding(horizontal = 10.dp,vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Select pets", fontSize = 24.sp)
                    IconButton(onClick = {
//                        onConfirm(selectedPetId)
                        expanded = false
                    }) {
                        Icon(imageVector = Icons.Default.Check, tint = MainPurple, contentDescription = "")
                    }
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp, top = 4.dp))

                petsList.forEachIndexed { index,pet ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clickable {
                                onConfirm(selectedPet)
                            }
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                contentDescription = "",
                                painter = painterResource(R.drawable.cat)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = pet.petName)

                        }
                        RadioButton(
                            selected = selectedPet.id == pet.id,
                            onClick = null// handled by row click
                        )
                    }

                    // Add a divider after each item except for the last one
                    if (index < petsList.lastIndex){
                        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp).alpha(0.5f))

                    }
                }

                // TODO: maybe add Onconfirm button
            }


        }
    }
}


@Composable
fun PetDropDownMenuV2(
    petsList: List<Pet>,
    onConfirm: (Set<Pet>) -> Unit,
) {
//    val selectedPets = remember { mutableStateListOf(petsList[0]) }
    var selectedPets by remember { mutableStateOf(setOf(petsList[0])) }


    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        // the text input field
        OutlinedTextField(
            value = if (selectedPets.isNotEmpty()) selectedPets.first().petName else "Select Pet",
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(),
//        supportingText = {
//            if (error != null){
//                Text(error)
//            }
//        },
            trailingIcon = {
                val icon =
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                Icon(icon, "")
            },
            leadingIcon = {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.drawable.cat),
                    contentDescription = ""
                )
            },
            onValueChange = { },
            readOnly = true,
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { expanded = true },
            color = Color.Transparent,
        ) {}
    }


    // Pop up dialog
    if (expanded) {
        Dialog(
            onDismissRequest = {
                onConfirm(selectedPets)
                expanded = false
            })
        {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White).padding(horizontal = 10.dp,vertical = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Select pets", fontSize = 24.sp)
                    IconButton(onClick = {
                        onConfirm(selectedPets)
                        expanded = false
                    }) {
                        Icon(imageVector = Icons.Default.Check, tint = MainPurple, contentDescription = "")
                    }
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp, top = 4.dp))

                petsList.forEachIndexed { index,pet ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .clickable {
                                selectedPets = if (pet in selectedPets) selectedPets - pet
                                else selectedPets + pet
//                                    if (pet in selectedPets) selectedPets.remove(pet)
//                                    else selectedPets.add(pet)
                            }
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                contentDescription = "",
                                painter = painterResource(R.drawable.cat)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = pet.petName)

                        }
                        Checkbox(
                            checked = pet in selectedPets,
                            onCheckedChange = null // handled by row click
                        )
                    }

                    // Add a divider after each item except for the last one
                    if (index < petsList.lastIndex){
                        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp).alpha(0.5f))

                    }
                }

                // TODO: Onconfirm
            }


        }
    }
}