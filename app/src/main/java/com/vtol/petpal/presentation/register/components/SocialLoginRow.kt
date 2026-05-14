package com.vtol.petpal.presentation.register.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R

@Composable
fun SocialLoginRow(
    onFacebookClicked: () -> Unit,
    onGoogleClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = onFacebookClicked
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier.padding(vertical = 4.dp).size(20.dp),

                    painter = painterResource(R.drawable.ic_facebook),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Facebook", color= Color.Black)
            }
        }
        Button(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(
                width = 1.dp,
                color = Color.LightGray
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            onClick = onGoogleClicked
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Image(
                    modifier = Modifier.padding(vertical = 4.dp).size(20.dp),
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Google", color = Color.Black)
            }
        }
    }
}