package com.vtol.petpal.presentation.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.pets.components.PetCard
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PetsScreen(){

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ){
        items(2){
            PetCard()
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PetsPreview () {
    PetPalTheme {
        PetsScreen()
    }
}