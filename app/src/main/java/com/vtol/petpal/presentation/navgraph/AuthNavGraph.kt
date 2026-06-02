package com.vtol.petpal.presentation.navgraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
        composable(
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(400),
                    initialOffsetX = { it }
                ) + fadeIn(animationSpec = tween(400))
            },

            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(400),
                    targetOffsetX = { -it } // Notice the minus sign (-)
                ) + fadeOut(animationSpec = tween(400))
            },

            popEnterTransition = {
                slideInHorizontally(
                    animationSpec = tween(400),
                    initialOffsetX = { -it } // Comes back from the left
                ) + fadeIn(animationSpec = tween(400))
            },

            popExitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(400),
                    targetOffsetX = { it }
                ) + fadeOut(animationSpec = tween(400))
            },
            route = Routes.GetStartedScreen.route
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.AuthGraph.route)
            }

            val context = LocalContext.current

            val loginViewModel: LoginViewModel = hiltViewModel(parentEntry)
            GetStartedScreen(
                onGoogleClicked = { loginViewModel.onEvent(LoginEvent.GoogleClicked(context)) },
                onFacebookClicked = { loginViewModel.onEvent(LoginEvent.FacebookClicked(it)) },
                navigateToLogin = { navController.navigate(Routes.LoginScreen.route) },
                navigateToSignUp = { navController.navigate(Routes.SignUpScreen.route) }
            )
        }

        composable(
            route = Routes.LoginScreen.route,
            // 1. Moving Forward: Screen slides up from the bottom while fading in
            enterTransition = {
                slideInVertically(animationSpec = tween(350)) { it } + fadeIn(
                    animationSpec = tween(
                        350
                    )
                )
            },
            // 2. Moving Forward Away: Screen slides up and off the top while fading out
            exitTransition = {
                slideOutVertically(animationSpec = tween(350)) { -it } + fadeOut(
                    animationSpec = tween(
                        350
                    )
                )
            },
            // 3. Returning Back: When coming back from SignUp, it slides down from the top
            popEnterTransition = {
                slideInVertically(animationSpec = tween(350)) { -it } + fadeIn(
                    animationSpec = tween(
                        350
                    )
                )
            },
            // 4. Hitting Back: When dismissing Login to go back to GetStarted, it slides down to the bottom
            popExitTransition = {
                slideOutVertically(animationSpec = tween(350)) { it } + fadeOut(
                    animationSpec = tween(
                        350
                    )
                )
            }
        ) {
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

        composable(
            route = Routes.SignUpScreen.route,
            // Keeps the exact same consistent animation language as the Login Screen
            enterTransition = {
                slideInVertically(animationSpec = tween(350)) { it } + fadeIn(
                    animationSpec = tween(
                        350
                    )
                )
            },
            exitTransition = {
                slideOutVertically(animationSpec = tween(350)) { -it } + fadeOut(
                    animationSpec = tween(
                        350
                    )
                )
            },
            popEnterTransition = {
                slideInVertically(animationSpec = tween(350)) { -it } + fadeIn(
                    animationSpec = tween(
                        350
                    )
                )
            },
            popExitTransition = {
                slideOutVertically(animationSpec = tween(350)) { it } + fadeOut(
                    animationSpec = tween(
                        350
                    )
                )
            }
        ) {
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