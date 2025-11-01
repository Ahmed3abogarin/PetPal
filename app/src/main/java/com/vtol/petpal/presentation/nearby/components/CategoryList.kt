package com.vtol.petpal.presentation.nearby.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.PlaceCategory
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun CategoryList(
    modifier: Modifier,
    selectCategory: PlaceCategory,
    onCategoryClicked: (PlaceCategory) -> Unit,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(Color.White)
                .padding(start = 6.dp, bottom = 12.dp, top = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Nearby", style = MaterialTheme.typography.displaySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(PlaceCategory.entries) {
//            FilterChip()
                Card(
                    onClick = { onCategoryClicked(it) },
                    colors = CardDefaults.cardColors(containerColor = if (selectCategory == it) MainPurple else Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(18.dp)
                                .padding(end = 3.dp),
                            contentDescription = "",
                            painter = painterResource(it.image)
                        )
                        Text(
                            text = it.displayName,
                            fontSize = 18.sp
                        )
                    }
                }
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