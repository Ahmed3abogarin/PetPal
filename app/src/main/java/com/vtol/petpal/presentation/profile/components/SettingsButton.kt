package com.vtol.petpal.presentation.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun SettingsButton(
    buttonTxt: String = "Settings",
    description: String? = null,
    trailingText: String? = null,
    txtColor: Color = Color.Black,
    icon: Int,
    bgColor: Color = Color(0XFFEFE9FF),
    clickable: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(clickable) { onClick() }
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
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(icon),
                    contentDescription = "",
                )
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = buttonTxt,
                    fontSize = 14.sp,
                    color = txtColor,
                    fontWeight = FontWeight.Medium
                )
                description?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

        }
        Row(verticalAlignment = Alignment.CenterVertically){
            trailingText?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Normal
                )
            }

            if (clickable) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    tint = MainPurple.copy(alpha = 0.3f),
                    contentDescription = ""
                )
            }
        }

    }

}

@Preview
@Composable
fun ButtonPreview() {
    PetPalTheme {
        SettingsButton(
            buttonTxt = "Upgrade to Premium",
            icon = R.drawable.ic_crown
        ) { }
    }
}