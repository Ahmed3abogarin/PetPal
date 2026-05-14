package com.vtol.petpal.presentation.register.login

import android.widget.Toast
import com.vtol.petpal.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.register.signup.secondFilledTextFieldColors
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun LoginScreen(
    state: LoginUiState,
    event: (LoginEvent) -> Unit,
    navigateToSignUp: () -> Unit
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {

        // Background top right circles
        Canvas(modifier = Modifier.align(Alignment.TopEnd)) {
            drawCircle(color = LightPurple.copy(alpha = 0.3f), radius = 600f)
            drawCircle(
                color = LightPurple.copy(alpha = 0.3f),
                radius = 640f,
                style = Stroke(width = (1.dp).toPx())
            )
        }

        // Background bottom start squares
        Canvas(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-62).dp)
                .rotate(25f)
        ) {
            drawRect(
                color = LightPurple.copy(alpha = 0.5f),
                style = Stroke(width = (1.dp).toPx())
            )
        }
        Canvas(
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-62).dp)
                .rotate(70f)
        ) {
            drawRect(
                color = LightPurple.copy(alpha = 0.5f),
                style = Stroke(width = (1.dp).toPx())
            )
        }



        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Login here",
                color = MainPurple,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Welcome back you’ve\nbeen missed!",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(36.dp))


            AppTextField(
                value = state.email,
                leadingIcon = Icons.Default.Email,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Email",
                isOneLine = true,
                errorTxt = state.emailError,
                onValueChanged = { event(LoginEvent.EmailChanged(it)) }
            )

            AppTextField(
                value = state.password,
                leadingIcon = Icons.Default.Lock,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Password",
                isOneLine = true,
                password = true,
                errorTxt = state.passwordError,
                onValueChanged = { event(LoginEvent.PasswordChanged(it)) }
            )

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {

                    },
                text = "Forget your password?",
                color = MainPurple,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )


            Spacer(modifier = Modifier.height(24.dp))

            SaveButton(
                text = "Sign in",
                color = MainPurple,
            ) {
                event(LoginEvent.LoginClicked)
            }

            Spacer(modifier = Modifier.height(34.dp))


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
            Spacer(modifier = Modifier.height(14.dp))

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
                    onClick = {
                        event(LoginEvent.FacebookClicked)
                    }
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
                    onClick = {
                        event(LoginEvent.GoogleClicked)
                    }
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


            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", fontWeight = FontWeight.Medium)
                Text(
                    modifier = Modifier.clickable { navigateToSignUp() },
                    text = "Create account", color = MainPurple, fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        state = LoginUiState(),
        event = {}
    ) {}
}