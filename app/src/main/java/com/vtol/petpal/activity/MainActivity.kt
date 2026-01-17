package com.vtol.petpal.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vtol.petpal.presentation.navgraph.AuthNavGraph
import com.vtol.petpal.presentation.navgraph.MainNavGraph
import com.vtol.petpal.presentation.navgraph.components.ErrorScreen
import com.vtol.petpal.presentation.onboarding.OnboardingScreen
import com.vtol.petpal.presentation.register.AuthState
import com.vtol.petpal.presentation.register.RegisterViewModel
import com.vtol.petpal.presentation.splash.SplashScreen
import com.vtol.petpal.ui.theme.PetPalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetPalTheme {
                Column(modifier = Modifier.fillMaxSize()) {

                    val viewModel: RegisterViewModel = hiltViewModel()
                    RootScreen(viewModel)

                }
            }
        }
    }
}

@Composable
fun RootScreen(
    authViewModel: RegisterViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        AuthState.Loading -> {
            SplashScreen()
        }

        AuthState.Unauthenticated -> {
            AuthNavGraph()
        }

        AuthState.OnBoarding -> {
            OnboardingScreen {
                authViewModel.completeOnBoarding()
            }
        }

        is AuthState.Authenticated -> {
            MainNavGraph()
        }

        is AuthState.Error -> {
            ErrorScreen(
                message = (authState as AuthState.Error).message
            )
        }
    }
}
