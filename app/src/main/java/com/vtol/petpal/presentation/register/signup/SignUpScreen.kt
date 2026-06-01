package com.vtol.petpal.presentation.register.signup

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.R
import com.vtol.petpal.presentation.common.components.LoadingIndicator
import com.vtol.petpal.presentation.common.components.rememberFacebookAuthLauncher
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.components.secondFilledTextFieldColors
import com.vtol.petpal.presentation.register.components.SocialLoginRow
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun SignUpScreen(
    state: SignUpUiState,
    event: (SignUpEvent) -> Unit,
    navigateToLogin: () -> Unit
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
            event(SignUpEvent.ErrorShown)
        }
    }

    val launchFacebook = rememberFacebookAuthLauncher(
        onSuccess = { token -> event(SignUpEvent.FacebookClicked(token)) }
    )
    SignUpContent(
        state = state,
        event = event,
        navigateToLogin = navigateToLogin,
        onGoogleClicked = { event(SignUpEvent.GoogleClicked(context)) },
        onFacebookClicked = { launchFacebook() }
    )

}

@Composable
fun SignUpContent(
    state: SignUpUiState,
    event: (SignUpEvent) -> Unit,
    navigateToLogin: () -> Unit,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding()
    ) {

        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(alignment = Alignment.TopEnd)
                .offset(y = (118).dp, x = (-22).dp)
                .rotate(45f),
            painter = painterResource(R.drawable.ic_pets_filled),
            tint = LightPurple.copy(alpha = 0.4f),
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(alignment = Alignment.TopStart)
                .offset(y = (100).dp, x = (22).dp)
                .rotate(-45f),
            painter = painterResource(R.drawable.ic_pets_filled),
            tint = LightPurple.copy(alpha = 0.4f),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Image(
                modifier = Modifier
                    .size(56.dp),
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "app logo"
            )
            Text(
                text = "PetPal",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MainPurple
            )

            Spacer(modifier= Modifier.height(16.dp))

            Text(
                "Create Account",
                color = MainPurple,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                "Create an account to continue and manage your pet’s care easily",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            AppTextField(
                value = state.name,
                leadingIcon = R.drawable.ic_person_v2,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Name",
                errorTxt = state.nameError,
                isOneLine = true,
                onValueChanged = { event(SignUpEvent.NameChanged(it)) }
            )

            AppTextField(
                value = state.email,
                leadingIcon = R.drawable.ic_mail,
                colors = secondFilledTextFieldColors(),
                isOneLine = true,
                errorTxt = state.emailError,
                placeHolder = "Email",
                onValueChanged = { event(SignUpEvent.EmailChanged(it)) }
            )

            AppTextField(
                value = state.password,
                leadingIcon = R.drawable.ic_lock,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Password",
                errorTxt = state.passwordError,
                isOneLine = true,
                password = true,
                onValueChanged = { event(SignUpEvent.PasswordChanged(it)) }
            )


            SaveButton(
                text = "Sign Up",
                color = MainPurple,
            ) {
                // handle the sign up click
                event(SignUpEvent.SignUpClicked)

            }


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

            SocialLoginRow(
                onGoogleClicked = onGoogleClicked,
                onFacebookClicked = onFacebookClicked
            )


            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    "Already have an account? ",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier.clickable { navigateToLogin() },
                    text = "Sign In", color = MainPurple, fontWeight = FontWeight.SemiBold
                )
            }
        }
    }


    if (state.isLoading) {
        LoadingIndicator()
    }

}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpContent(
        state = SignUpUiState(),
        event = {},
        navigateToLogin = {},
        onGoogleClicked = {},
        onFacebookClicked = {}
    )
}