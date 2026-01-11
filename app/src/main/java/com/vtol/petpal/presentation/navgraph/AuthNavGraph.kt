package com.vtol.petpal.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.presentation.register.LoginScreen
import com.vtol.petpal.presentation.register.SignUpScreen

@Composable
fun AuthNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                navigateToLogin = {
                    navController.navigate("login")
                }
            )
        }
    }

}