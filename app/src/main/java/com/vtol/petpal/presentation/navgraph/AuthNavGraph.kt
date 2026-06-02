package com.vtol.petpal.presentation.navgraph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vtol.petpal.presentation.register.GetStartedScreen
import com.vtol.petpal.presentation.register.login.LoginEvent
import com.vtol.petpal.presentation.register.login.LoginScreen
import com.vtol.petpal.presentation.register.login.LoginViewModel
import com.vtol.petpal.presentation.register.signup.SignUpScreen
import com.vtol.petpal.presentation.register.signup.SignUpViewModel

fun NavGraphBuilder.authNavGraph(navController: NavController) {

    navigation(
        startDestination = Routes.GetStartedScreen.route,
        route = Routes.AuthGraph.route
    ) {
        composable(Routes.GetStartedScreen.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.AuthGraph.route)
            }

            val context = LocalContext.current

            val loginViewModel: LoginViewModel = hiltViewModel(parentEntry)
            GetStartedScreen(
                onGoogleClicked = { loginViewModel.onEvent(LoginEvent.GoogleClicked(context)) },
                onFacebookClicked = { loginViewModel.onEvent(LoginEvent.FacebookClicked(it))},
                navigateToLogin = { navController.navigate(Routes.LoginScreen.route) },
                navigateToSignUp = { navController.navigate(Routes.SignUpScreen.route) }
            )
        }


        composable(Routes.LoginScreen.route) {
            val loginVm: LoginViewModel = hiltViewModel()
            val state by loginVm.uiState.collectAsState()

            LoginScreen(
                state = state,
                event = loginVm::onEvent,
                navigateToSignUp = {
                    navController.navigate(Routes.SignUpScreen.route)
                }
            )
        }

        composable(Routes.SignUpScreen.route) {
            val signUpVm: SignUpViewModel = hiltViewModel()
            val state by signUpVm.uiState.collectAsState()

            SignUpScreen(
                state = state,
                event = signUpVm::onEvent,
                navigateToLogin = {
                    navController.navigate(Routes.LoginScreen.route)
                }
            )
        }
    }
}