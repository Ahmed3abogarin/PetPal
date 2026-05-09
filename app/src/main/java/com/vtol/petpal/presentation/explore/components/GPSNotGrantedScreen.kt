package com.vtol.petpal.presentation.explore.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.domain.model.map.PlaceCategory
import com.vtol.petpal.presentation.components.AppIconButton
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.CellsBgPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import com.vtol.petpal.util.AppColors.petPalGradient

@Composable
fun GPSNotGrantedScreen(
    message: String = "Allow PetPal to use your location to find nearby vets, pet stores, parks and pharmacies",
    buttonTxt: String = "Allow location access",
    onAllowClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .background(petPalGradient)
                .padding(top = 16.dp)
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                AppIconButton(
                    icon = R.drawable.ic_search,
                ) {
                }
            }
            CategoryList(
                enabled = true,
                modifier = Modifier.padding(start = 16.dp),
                onCategoryClicked = {
                },
                selectCategory = PlaceCategory.VETS
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))
        // The blur layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
                .background(Color.White, shape = RoundedCornerShape(14.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val list = listOf(
                Color(0xFFC1A0F2),
                Color(0x599A9999),
            )

            Box(
                modifier = Modifier
                    .size(88.dp)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                PulsingCircle()
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(width = 3.dp, color = Color.White, shape = CircleShape)
                        .size(78.dp)
                        .background(brush = Brush.horizontalGradient(list)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(R.drawable.ic_location_locked),
                        contentDescription = ""
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Location access needed", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))


            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .fillMaxWidth()
                    .background(CellsBgPurple)
                    .padding(14.dp)
            ) {
                Text(
                    text = "WE USE LOCATION TO SHOW YOU:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MainPurple,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocalHospital,
                            tint = Color.Green,
                            contentDescription = null
                        )
                        Text("Nearest vets & clinics", fontSize = 14.sp)
                    }
                    Text("distance", fontSize = 12.sp, color = MainPurple)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            tint = Color(0XFFFF7110), contentDescription = null
                        )
                        Text("Pet stores & parks", fontSize = 14.sp)
                    }
                    Text("directions", fontSize = 12.sp, color = MainPurple)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocationOn, tint = MainPurple, contentDescription = null)
                        Text("Open hours near you", fontSize = 14.sp)
                    }

                    Text("real-time", fontSize = 12.sp, color = MainPurple)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Your location is never stored or shared",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MainPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MainPurple),
                onClick = onAllowClicked
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(R.drawable.ic_location),
                        contentDescription = null
                    )
                    Text(buttonTxt)
                }
            }
        }
    }
}

@Preview
@Composable
fun GpsPreview() {
    PetPalTheme {
        GPSNotGrantedScreen {}

    }
}