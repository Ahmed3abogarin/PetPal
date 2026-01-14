package com.vtol.petpal.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.presentation.register.LoginScreen
import com.vtol.petpal.presentation.register.RegisterViewModel
import com.vtol.petpal.presentation.register.SignUpScreen

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()
    val viewmodel: RegisterViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = viewmodel,
                navigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                viewModel = viewmodel,
                navigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
    }

}