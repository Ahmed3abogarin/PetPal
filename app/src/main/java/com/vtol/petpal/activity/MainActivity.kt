package com.vtol.petpal.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vtol.petpal.presentation.navgraph.AuthNavGraph
import com.vtol.petpal.presentation.navgraph.MainNavGraph
import com.vtol.petpal.presentation.navgraph.components.ErrorScreen
import com.vtol.petpal.presentation.onboarding.OnboardingScreen
import com.vtol.petpal.presentation.register.AuthState
import com.vtol.petpal.presentation.register.login.LoginViewModel
import com.vtol.petpal.presentation.splash.SplashScreen
import com.vtol.petpal.presentation.update.FlexibleUpdateScreen
import com.vtol.petpal.presentation.update.ImmediateUpdateScreen
import com.vtol.petpal.presentation.update.UpdateState
import com.vtol.petpal.presentation.update.UpdateViewModel
import com.vtol.petpal.ui.theme.PetPalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetPalTheme {
                val updateViewModel: UpdateViewModel = hiltViewModel()
                val authViewModel: LoginViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    updateViewModel.getMinRequiredVersion()
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    RootScreen(authViewModel, updateViewModel)
                }
            }
        }
    }
}

@Composable
fun RootScreen(
    authViewModel: LoginViewModel,
    updateViewModel: UpdateViewModel
) {
    val updateState by updateViewModel.updateState.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    when (updateState) {
        is UpdateState.Loading -> SplashScreen()
        is UpdateState.Immediate -> ImmediateUpdateScreen()  // show Immediate update dialog or screen
        is UpdateState.Flexible -> FlexibleUpdateScreen {

            // For Flexible updates I want to show a pop up dialog when the user navigate to Home screen,
            // I have two options:
            // 1- Keep get version as it is and save the status (true/false) in local preferences
            // 2- Modify get version function in Update VM to check only for Immediate update version, and in Home VM create another function to check only for Flexible update version
            updateViewModel.dismissFlexibleUpdate()
        } // show flexible update process
        is UpdateState.UpToDate -> {
            when (authState) {
                is AuthState.Loading -> {
                    SplashScreen()
                }

                is AuthState.Unauthenticated -> {
                    AuthNavGraph()
                }

                is AuthState.OnBoarding -> {
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
    }

}
