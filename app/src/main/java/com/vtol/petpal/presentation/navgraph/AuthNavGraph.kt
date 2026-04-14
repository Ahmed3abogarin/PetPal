package com.vtol.petpal.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.presentation.register.login.LoginScreen
import com.vtol.petpal.presentation.register.login.LoginViewModel
import com.vtol.petpal.presentation.register.signup.SignUpScreen
import com.vtol.petpal.presentation.register.signup.SignUpViewModel

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()
    val signUpVm: SignUpViewModel = hiltViewModel()
    val loginVm: LoginViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                navigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = signUpVm,
                navigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
    }

}