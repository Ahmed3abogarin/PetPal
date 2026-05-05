package com.vtol.petpal.presentation.explore.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.domain.model.map.PlaceCategory
import com.vtol.petpal.ui.theme.ExtraLightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    selectCategory: PlaceCategory,
    onCategoryClicked: (PlaceCategory) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(PlaceCategory.entries) {

            val isSelected = selectCategory == it
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else ExtraLightPurple
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) MainPurple else Color.White
            )
//            FilterChip()
            Card(
                onClick = { onCategoryClicked(it) },
                colors = CardDefaults.cardColors(containerColor = bgColor),
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 3.dp),
                        contentDescription = "",
                        tint = textColor,
                        painter = painterResource(it.image)
                    )
                    Text(
                        text = it.displayName,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MyPreview() {
    PetPalTheme {
        CategoryList(
            modifier = Modifier,
            onCategoryClicked = {},
            selectCategory = PlaceCategory.VETS
        )
    }
}