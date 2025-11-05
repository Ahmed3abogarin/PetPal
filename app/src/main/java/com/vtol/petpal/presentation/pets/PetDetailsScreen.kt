package com.vtol.petpal.presentation.pets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vtol.petpal.domain.model.Pet
import com.vtol.petpal.util.Resource

@Composable
fun PetDetailsScreen(modifier: Modifier = Modifier, petViewModel: PetDetailsViewModel) {
    val state by petViewModel.petState.collectAsState()

    PetDetailsScreenContent(modifier = modifier,state=  state)
}

@Composable
private fun PetDetailsScreenContent(modifier: Modifier = Modifier, state: Resource<Pet?>){

    Box(modifier= modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        when(state){
            is Resource.Success -> {
                Text(text = state.data?.petName ?: "couldn't load pet")

            }
            is Resource.Error -> {
                Text(state.message)
            }
            is Resource.Loading -> {
                CircularProgressIndicator()

            }
        }
    }



}