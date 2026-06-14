package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme


@Composable
fun CloudSyncButton(
    bgColor: Color = Color(0XFFEFE9FF),
    textColor: Color,
    text: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(bgColor)
                    .padding(14.dp)
                    .size(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Outlined.Cloud,
                    tint = MainPurple,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Cloud Sync",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                    Text(
                        text = text,
                        fontSize = 12.sp,
                        color = textColor,
                        fontWeight = FontWeight.Normal
                    )
            }

        }
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF6C47FF),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFDDD8F0),
                uncheckedBorderColor = Color.Transparent,
                checkedBorderColor = Color.Transparent,
            )
        )

    }

}


@Preview
@Composable
fun CloudSyncButtonPreview() {
    PetPalTheme {
        CloudSyncButton(
            isEnabled = false,
            text = "",
            textColor = Color.DarkGray
        ) { }
    }
}