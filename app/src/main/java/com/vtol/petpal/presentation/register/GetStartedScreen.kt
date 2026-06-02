package com.vtol.petpal.presentation.register

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.onboarding.FloatingHeart
import com.vtol.petpal.presentation.onboarding.PetPalTopBar
import com.vtol.petpal.presentation.register.components.SocialLoginRow
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme

@Composable
fun GetStartedScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        PetPalTopBar(modifier = Modifier.align(Alignment.TopCenter))


        Column(modifier = Modifier.align(Alignment.BottomCenter)) {

            Box(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "Because",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "every",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = " bond",
                            fontSize = 40.sp,
                            color = MainPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "matters.",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Join PetPal and give \nyour pet the best care,\nevery day.",
                        color = Color.DarkGray
                    )
                }


                FloatingHeart(
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-32).dp, y = 24.dp)
                )
                FloatingHeart(
                    Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 8.dp)
                )
                FloatingHeart(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-12).dp)
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 32.dp)
                ) {
                    Spacer(modifier = Modifier.height(44.dp))

                    Image(
                        modifier = Modifier
                            .height(270.dp)
                            .width(200.dp),
                        painter = painterResource(R.drawable.get_started_img),
                        contentDescription = "onboarding image"
                    )

                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(top = 36.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.6f),
                                    Color.White.copy(alpha = 0.98f),
                                    Color.White
                                )
                            )
                        )

                )


            }

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(bottom = 38.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                SaveButton(text = "Get started", icon = Icons.Default.Pets, color = MainPurple) {
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color(0xFFD0BCFF),
                        contentColor = Color.White,
                    ),
                    border = BorderStroke(color = MainPurple, width = 1.dp),
                    onClick = {}
                ) {
                    Text(
                        modifier = modifier.padding(vertical = 6.dp),
                        text = "Log In",
                        fontSize = 18.sp,
                        color = MainPurple
                    )

                }


                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = "Or continue with",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))

                SocialLoginRow(
                    onGoogleClicked = {},
                    onFacebookClicked = {}
                )
                Spacer(modifier = Modifier.height(38.dp))


                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(R.drawable.ic_security),
                        contentDescription = null
                    )

                    Text(
                        text = "Your pet’s data is safe with us",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }


            }
        }
    }
}

@Preview
@Composable
fun GetStartedScreenPreview() {
    PetPalTheme {
        GetStartedScreen()
    }
}