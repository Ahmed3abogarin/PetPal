package com.vtol.petpal.presentation.register.signup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.register.components.SocialLoginRow
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import com.vtol.petpal.ui.theme.SemiTransparentPurple

@Composable
fun SignUpScreen(
    state: SignUpUiState,
    event: (SignUpEvent) -> Unit,
    navigateToLogin: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            focusManager.clearFocus()
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
            drawCircle(color = LightPurple.copy(alpha = 0.3f), radius = 500f)
            drawCircle(
                color = LightPurple.copy(alpha = 0.3f),
                radius = 540f,
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
                "Create Account",
                color = MainPurple,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Create an account to continue and manage your pet’s care easily",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(36.dp))

            AppTextField(
                value = state.name,
                leadingIcon = Icons.Default.Person,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Name",
                errorTxt = state.nameError,
                isOneLine = true,
                onValueChanged = { event(SignUpEvent.NameChanged(it)) }
            )

            AppTextField(
                value = state.email,
                leadingIcon = Icons.Default.Email,
                colors = secondFilledTextFieldColors(),
                isOneLine = true,
                errorTxt = state.emailError,
                placeHolder = "Email",
                onValueChanged = { event(SignUpEvent.EmailChanged(it)) }
            )

            AppTextField(
                value = state.password,
                leadingIcon = Icons.Default.Lock,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Password",
                errorTxt = state.passwordError,
                isOneLine = true,
                password = true,
                onValueChanged = { event(SignUpEvent.PasswordChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SaveButton(
                text = "Sign Up",
                color = MainPurple,
            ) {
                // handle the sign up click
                event(SignUpEvent.SignUpClicked)

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



            SocialLoginRow(
                onGoogleClicked = { event(SignUpEvent.GoogleClicked("")) },
                onFacebookClicked = {
                    // TODO
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", fontWeight = FontWeight.Medium)
                Text(
                    modifier = Modifier.clickable { navigateToLogin() },
                    text = "Sign In", color = MainPurple, fontWeight = FontWeight.SemiBold
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


@Composable
fun secondFilledTextFieldColors() = TextFieldDefaults.colors(
    disabledTextColor = Color.Black,
    disabledContainerColor = SemiTransparentPurple,
    focusedContainerColor = SemiTransparentPurple,
    unfocusedContainerColor = SemiTransparentPurple,
    disabledIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    errorContainerColor = SemiTransparentPurple,
    errorPlaceholderColor = Color.Black,
    errorTextColor = Color.Black,
    errorLabelColor = Color.Black,
)

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        state = SignUpUiState(),
        event = {}
    ) {}
}