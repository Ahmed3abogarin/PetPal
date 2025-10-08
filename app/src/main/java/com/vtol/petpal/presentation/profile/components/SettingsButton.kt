package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.VeryLightPurple

@Composable
fun SettingsButton(leadIcon: Int? = null, text: String ){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp).padding(bottom = 14.dp),
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = VeryLightPurple),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leadIcon?.let {
                    Icon(
                        tint = Color.Black,
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(leadIcon),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Text(text = text, fontSize = 22.sp, color = Color.Black)
            }
            Icon(
                tint = Color.Black,
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_arrow),
                contentDescription = "arrow icon"
            )

        }
    }
}