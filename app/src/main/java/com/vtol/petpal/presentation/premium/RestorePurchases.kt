package com.vtol.petpal.presentation.premium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.showToast

@Composable
fun RestorePurchasesScreen(navigateUp: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding()
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.fillMaxWidth()) {
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                onClick = { navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "back arrow"
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Restore Purchases",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier.size(180.dp),
            painter = painterResource(R.drawable.img_restore_purchases),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Restore Purchases",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            text = "If you already purchased premium on another device, you can restore it.",
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(38.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0ECFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = MainPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "Instant access to Premium",
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0ECFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = MainPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "Keep all your features",
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }


            Row(
                modifier = Modifier.align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0ECFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = MainPurple,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "No extra charges",
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(38.dp))

        SaveButton(
            text = "Restore",
            color = MainPurple
        ) { context.showToast() }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { context.showToast() }) {
            Text(
                text = "Need help?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MainPurple
            )
        }
    }

}


@Preview
@Composable
fun PurchasesPreview() {
    PetPalTheme {
        RestorePurchasesScreen {}
    }
}