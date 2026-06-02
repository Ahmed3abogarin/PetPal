package com.vtol.petpal.presentation.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.common.components.rememberFacebookAuthLauncher
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.onboarding.FloatingHeart
import com.vtol.petpal.presentation.onboarding.PetPalTopBar
import com.vtol.petpal.presentation.register.components.SocialLoginRow
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.PetPalTheme
import kotlinx.coroutines.delay

@Composable
fun GetStartedScreen(
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: (String) -> Unit
) {
    val launchFacebook = rememberFacebookAuthLauncher(
        onSuccess = { token -> onFacebookClicked(token) }
    )

    var visible by remember { mutableStateOf(false) }

    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        delay(150)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(top = 16.dp)
            .statusBarsPadding()
    ) {
        PetPalTopBar(modifier = Modifier.align(Alignment.TopCenter))


        Column(modifier = Modifier.align(Alignment.BottomCenter)) {

            Box(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier.padding(start = 16.dp)) {

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(animationSpec = tween(1000)),
                        exit = slideOutHorizontally()

                    ) {
                        Column {
                            Text(
                                text = "Because",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))

                            // Safe multicolored text using AnnotatedString
                            Text(
                                text = buildAnnotatedString {
                                    append("every")
                                    withStyle(style = SpanStyle(color = MainPurple)) {
                                        append(" bond")
                                    }
                                },
                                fontSize = 38.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "matters.",
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                                FloatingHeart(
                                    Modifier
                                        .padding(start = 8.dp)
                                        .size(24.dp)
                                )
                            }
                        }
                    }


                    Spacer(Modifier.height(16.dp))

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(animationSpec = tween(1200)),
                        exit = slideOutHorizontally()
                    ) {
                        Text(
                            text = "Join PetPal and give \nyour pet the best care,\nevery day.",
                            color = Color.DarkGray
                        )
                    }


                }


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 32.dp)
                ) {

                    this@Column.AnimatedVisibility(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                            .offset(x = 62.dp),
                        visible = visible,
                        enter = scaleIn(animationSpec = tween(1000)),
                        exit = slideOutHorizontally()
                    ) {
                        val circleRadiusPx = with(density) { 120.dp.toPx() }
                        Canvas(modifier = Modifier.size(150.dp)) {
                            drawCircle(
                                radius = circleRadiusPx,
                                color = MainPurple.copy(alpha = 0.15f)
                            )
                        }
                    }

                    this@Column.AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(animationSpec = tween(1000)) { it },
                        exit = slideOutHorizontally()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(26.dp))

                            Image(
                                modifier = Modifier
                                    .height(270.dp)
                                    .width(200.dp),
                                painter = painterResource(R.drawable.get_started_img),
                                contentDescription = "onboarding image"
                            )

                        }

                    }


                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(80.dp)
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
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(1500))
                ) {

                    Column {
                        SaveButton(
                            text = "Get started",
                            icon = Icons.Default.Pets,
                            color = MainPurple
                        ) {
                            navigateToSignUp()
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(color = MainPurple, width = 1.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MainPurple),
                            onClick = navigateToLogin
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 6.dp),
                                text = "Log In",
                                fontSize = 18.sp
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
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        SocialLoginRow(
                            onGoogleClicked = onGoogleClicked,
                            onFacebookClicked = { launchFacebook() }
                        )

                        Spacer(modifier = Modifier.height(38.dp))

                        Row(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(14.dp),
                                painter = painterResource(R.drawable.ic_security),
                                contentDescription = null
                            )

                            Text(
                                text = "Your pet’s data is safe with us",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GetStartedScreenPreview() {
    PetPalTheme {
        GetStartedScreen(navigateToLogin = {}, navigateToSignUp = {}, onGoogleClicked = {}, { })
    }
}