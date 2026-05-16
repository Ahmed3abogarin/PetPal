package com.vtol.petpal.presentation.register.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vtol.petpal.R
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.components.secondFilledTextFieldColors
import com.vtol.petpal.presentation.register.components.SocialLoginRow
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
        if (state.isLoading) focusManager.clearFocus()
    }
    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

            // TODO: Clear the error state
        }
    }

    val callbackManager = remember { CallbackManager.Factory.create() }
    val loginLauncher = rememberLauncherForActivityResult(
        contract = LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    ) { _ -> }

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    event(LoginEvent.FacebookClicked(result.accessToken.token))
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException) {}
            }
        )
        onDispose { LoginManager.getInstance().unregisterCallback(callbackManager) }
    }

    LoginContent(
        state = state,
        event = event,
        navigateToSignUp = navigateToSignUp,
        onGoogleClicked = { event(LoginEvent.GoogleClicked(context)) },
        onFacebookClicked = { loginLauncher.launch(listOf("email", "public_profile")) }
    )
}


@Composable
fun LoginContent(
    state: LoginUiState,
    event: (LoginEvent) -> Unit,
    navigateToSignUp: () -> Unit,
    onGoogleClicked: () -> Unit,
    onFacebookClicked: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
            .statusBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
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
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(56.dp),
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "app logo"
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "PetPal",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MainPurple
            )

        }



        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Welcome back!",
                color = MainPurple,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold)
            )


            Text(
                "We missed you and your pets.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )

            Spacer(modifier = Modifier.height(12.dp))


            AppTextField(
                value = state.email,
                leadingIcon = R.drawable.ic_mail,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Email",
                isOneLine = true,
                errorTxt = state.emailError,
                onValueChanged = { event(LoginEvent.EmailChanged(it)) }
            )

            AppTextField(
                value = state.password,
                leadingIcon = R.drawable.ic_lock,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Password",
                isOneLine = true,
                password = true,
                errorTxt = state.passwordError,
                onValueChanged = { event(LoginEvent.PasswordChanged(it)) }
            )
            Spacer(modifier = Modifier.height(2.dp))

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
            Spacer(modifier = Modifier.height(2.dp))


            SaveButton(
                text = "Sign in",
                color = MainPurple,
            ) {
                event(LoginEvent.LoginClicked)
            }

            Spacer(modifier = Modifier.height(8.dp))

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

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", color = Color.Gray, fontWeight = FontWeight.Medium)
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
    LoginContent(
        state = LoginUiState(),
        onGoogleClicked = {},
        onFacebookClicked = {},
        event = {},
        navigateToSignUp = {}
    )
}