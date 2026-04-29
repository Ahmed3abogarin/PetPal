package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.TextPurple

@Composable
fun PetInfoItem(modifier: Modifier = Modifier, title: String, subTitle: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CellsBgPurple)
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = LightPurple)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subTitle,
                color = TextPurple,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

        }

    }
}