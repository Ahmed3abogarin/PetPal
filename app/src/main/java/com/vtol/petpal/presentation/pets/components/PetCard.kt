package com.vtol.petpal.presentation.pets.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun PetCard() {
    Card(
        onClick = {},
        colors = CardDefaults.cardColors(containerColor = LightPurple),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
            Row {
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    painter = painterResource(R.drawable.cat),
                    contentDescription = "pet photo"
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = "Blind Pew", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "6 months • British Shorthair",
                        fontSize = 13.sp,
                        color = Color.LightGray
                    )
                    Text(text = "\uD83D\uDC8A Needs meds today")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.weight(1f), onClick = {}) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)) {
                        Image(
                            modifier = Modifier.size(26.dp).align(Alignment.CenterStart),
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = ""
                        )
                        Text(
                            text = "Edit",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 8.dp)
                        )
                    }
                }

                Card(modifier = Modifier.weight(1f), onClick = {}) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)) {
                        Image(
                            modifier = Modifier.size(26.dp).align(Alignment.CenterStart),
                            painter = painterResource(R.drawable.ic_calender),
                            contentDescription = ""
                        )
                        Text(
                            text = "Edit",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }

        }
    }

}

@Preview
@Composable
fun PetCardPreview() {
    PetPalTheme {
        PetCard()
    }

}