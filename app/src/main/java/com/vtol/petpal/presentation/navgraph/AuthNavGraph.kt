package com.vtol.petpal.presentation.navgraph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.vtol.petpal.presentation.register.login.LoginScreen
import com.vtol.petpal.presentation.register.login.LoginViewModel
import com.vtol.petpal.presentation.register.signup.SignUpScreen
import com.vtol.petpal.presentation.register.signup.SignUpViewModel

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = Routes.LoginScreen.route,
        route = Routes.AuthGraph.route
    ) {
        composable(Routes.LoginScreen.route) {
            val loginVm: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = loginVm,
                navigateToSignUp = {
                    navController.navigate(Routes.SignUpScreen.route)
                }
            )
        }

        composable(Routes.SignUpScreen.route) {
            val signUpVm: SignUpViewModel = hiltViewModel()
            SignUpScreen(
                viewModel = signUpVm,
                navigateToLogin = {
                    navController.navigate(Routes.LoginScreen.route)
                }
            )
        }
    }
}