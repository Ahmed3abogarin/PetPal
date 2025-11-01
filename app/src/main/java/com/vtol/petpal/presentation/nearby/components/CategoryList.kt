package com.vtol.petpal.presentation.nearby.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun CategoryList(modifier: Modifier,selectCategory: PlaceCategory, onCategoryClicked: (PlaceCategory) -> Unit) {
    LazyRow (modifier = modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)){
        items(PlaceCategory.entries) {
//            FilterChip()
            Card(
                onClick = { onCategoryClicked(it) },
                colors = CardDefaults.cardColors(containerColor = if (selectCategory == it) MainPurple else Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), text = it.displayName, fontSize = 18.sp)
            }
        }
    }


}

//@Preview
//@Composable
//fun MyPreview() {
//    PetPalTheme {
//        CategoryList(listOf(""))
//    }
//}