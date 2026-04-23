package com.vtol.petpal.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.presentation.navgraph.components.ErrorScreen
import com.vtol.petpal.presentation.onboarding.OnboardingScreen
import com.vtol.petpal.presentation.register.AuthState
import com.vtol.petpal.presentation.splash.SplashScreen

@Composable
fun NavGraph() {

    val authViewModel: AuthViewModel = hiltViewModel()
    val navController = rememberNavController()

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> {} // stay on splash
            is AuthState.Authenticated -> navController.navigate(Routes.MainScreen.route) {
                popUpTo(0) { inclusive = true }
            }
            is AuthState.Unauthenticated -> navController.navigate(Routes.AuthGraph.route) {
                popUpTo(0) { inclusive = true }
            }
            is AuthState.OnBoarding -> navController.navigate(Routes.OnboardingScreen.route) {
                popUpTo(0) { inclusive = true }
            }
            is AuthState.Error -> navController.navigate(Routes.ErrorScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route
    ) {

        composable(Routes.SplashScreen.route) {
            SplashScreen()

        }

        composable(Routes.OnboardingScreen.route) {
            OnboardingScreen {
                authViewModel.completeOnBoarding()
            }
        }


        authNavGraph(navController)
        composable(Routes.MainScreen.route){
            MainScreen()
        }

        composable(Routes.ErrorScreen.route){
            ErrorScreen(
                message = (authState as AuthState.Error).message
            )
        }

    }
}