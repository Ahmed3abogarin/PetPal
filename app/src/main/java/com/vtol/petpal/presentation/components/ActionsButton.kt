package com.vtol.petpal.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ActionsButton(modifier: Modifier = Modifier,onClick: () -> Unit, buttonText: String,buttonIcon: ImageVector) {
    Card(
        modifier=modifier,
        onClick = { onClick() },
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0XFFE2D7F4))
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.padding(end = 12.dp, top = 6.dp, bottom = 6.dp),
                shape = CircleShape,
                color = Color.LightGray
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = buttonIcon,
                    contentDescription = ""
                )
            }

            Text(text = buttonText)
        }

    }

}