package com.vtol.petpal.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vtol.petpal.presentation.components.AppTextField
import com.vtol.petpal.presentation.components.SaveButton
import com.vtol.petpal.ui.theme.MainPurple

@Composable
fun LoginScreen(navigateToSignUp: () -> Unit) {

    var email by remember { mutableStateOf("") }


    var password by remember { mutableStateOf("") }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFFF8F4FF)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
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
                value = email,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Email",
                onValueChanged = { email = it }
            )

            AppTextField(
                value = password,
                colors = secondFilledTextFieldColors(),
                placeHolder = "Password",
                onValueChanged = { password = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SaveButton(
                text = "Sign in",
                color = MainPurple,
            ) {


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
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen{}
}