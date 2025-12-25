package com.vtol.petpal.presentation.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vtol.petpal.presentation.pets.components.PetCard

@Composable
fun PetsScreen(
    viewModel: PetViewModel,
    navigateToAddPetScreen: () -> Unit,
    onScheduleClick: (String) -> Unit, onCardClick: (String) -> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold (
        topBar = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                // text in the top middle
                Text(modifier = Modifier.padding(vertical = 12.dp), text = "My Pets", style = MaterialTheme.typography.displaySmall)
            }

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {navigateToAddPetScreen()}){
                Icon(Icons.Default.Add, contentDescription = "add pet button")
            }
        }
    ){ innerPadding ->

        when{
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                Text(text= state.error!!)
            }
            else -> {
                LazyColumn(
                    modifier= Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ){
                    items(state.pets){
                            pet -> PetCard(pet = pet, onScheduleClick = {onScheduleClick(it)}, onCardClick = {onCardClick(it)})
                    }
                }
            }

        }




    }



}


//@Preview(showBackground = true)
//@Composable
//fun PetsPreview () {
//    PetPalTheme {
//        PetsScreen(petViewModel.state.value) {}
//    }
//}