package com.vtol.petpal.presentation.register.login

import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.presentation.register.signup.secondFilledTextFieldColors
import com.vtol.petpal.ui.theme.BackgroundColor
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToSignUp: () -> Unit
) {

    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()


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
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Log in",
                color = MainPurple,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Welcome back! Continue managing your pet’s care",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(36.dp))


            AppTextField(
                value = state.email,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Email",
                isOneLine = true,
                errorTxt = state.emailError,
                onValueChanged = { viewModel.onEvent(LoginEvent.EmailChanged(it)) }
            )

            AppTextField(
                value = state.password,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Password",
                isOneLine = true,
                password = true,
                errorTxt = state.passwordError,
                onValueChanged = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SaveButton(
                text = "Sign in",
                color = MainPurple,
            ) {
                viewModel.onEvent(LoginEvent.LoginClicked)
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

//@Preview
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen{}
//}