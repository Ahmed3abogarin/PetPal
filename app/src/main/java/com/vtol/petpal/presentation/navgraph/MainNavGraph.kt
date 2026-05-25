package com.vtol.petpal.presentation.navgraph

import android.widget.Toast
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.vtol.petpal.presentation.calender.CalenderScreen
import com.vtol.petpal.presentation.calender.CalenderViewModel
import com.vtol.petpal.presentation.explore.ExploreScreen
import com.vtol.petpal.presentation.tasks.AddTaskScreen
import com.vtol.petpal.presentation.home.HomeScreen
import com.vtol.petpal.presentation.home.HomeViewModel
import com.vtol.petpal.presentation.add_pet.AddPetScreen
import com.vtol.petpal.presentation.add_pet.AddPetViewModel
import com.vtol.petpal.presentation.add_pet.UiEffects
import com.vtol.petpal.presentation.explore.ExploreViewModel
import com.vtol.petpal.presentation.pets.PetDetailsScreen
import com.vtol.petpal.presentation.pets.PetDetailsViewModel
import com.vtol.petpal.presentation.pets.PetViewModel
import com.vtol.petpal.presentation.pets.PetsScreen
import com.vtol.petpal.presentation.profile.FeedbackScreen
import com.vtol.petpal.presentation.profile.ProfileScreen
import com.vtol.petpal.presentation.profile.ProfileViewModel
import com.vtol.petpal.presentation.tasks.AddTaskUiEffect
import com.vtol.petpal.presentation.tasks.AddTaskViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(
        startDestination = Routes.HomeScreen.route,
        route = Routes.MainGraph.route
    ) {
        composable(Routes.HomeScreen.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val state by homeViewModel.state.collectAsState()

            HomeScreen(
                state = state,
                onAddTaskClicked = {
                    navController.navigate(Routes.AddTaskScreen.route)
                },
                onAddPetClicked = {
                    navController.navigate(Routes.AddPetScreen.route)
                },
                onPetClicked = {
                    navController.navigate(Routes.PetDetailsScreen.createRoute(it)) {
                        launchSingleTop = false
                    }
                },
                onToggleClicked = { taskId, isCompleted ->
                    homeViewModel.toggleCompletion(taskId, isCompleted)
                }
            )
        }
        composable(Routes.PetsScreen.route) {
            val viewmodel: PetViewModel = hiltViewModel()
            val state by viewmodel.state.collectAsState()

            PetsScreen(
                state = state,
                navigateToAddPetScreen = {
                    navController.navigate(Routes.AddPetScreen.route)
                },
                onScheduleClick = {
                    navController.navigate(Routes.AddTaskScreen.route)
                },
                onCardClick = {
                    navController.navigate(Routes.PetDetailsScreen.createRoute(it)) {
                        launchSingleTop = false
                    }
                }
            )
        }
        composable(Routes.CalenderScreen.route) {
            val calendarViewModel: CalenderViewModel = hiltViewModel()
            val state = calendarViewModel.state.collectAsState()
            CalenderScreen(state = state.value)
        }
        composable(
//                enterTransition = { slideInHorizontally(animationSpec = tween(400)) },
//                popExitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            route = Routes.NearbyScreen.route
        ) {
            val viewModel: ExploreViewModel = hiltViewModel()

            val state by viewModel.uiState.collectAsState()
            ExploreScreen(
                onCategoryClicked = {
                    viewModel.onCategorySelected(it)
                },
                state = state
            )
        }
        composable(route = Routes.ProfileScreen.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            val state by profileViewModel.uiState.collectAsState()


            ProfileScreen(
                state = state,
                event = profileViewModel::onEvent,
                petsCount = hiltViewModel<PetViewModel>().state.collectAsState().value.pets.size,
                doneTasks = hiltViewModel<HomeViewModel>().state.collectAsState().value.completedCount,
                navigateToFeedBack = {
                    navController.navigate(Routes.FeedbackScreen.route)
                }
            )
        }

        // sub screens
        composable(
            route = Routes.AddPetScreen.route,
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            enterTransition = { slideInVertically(initialOffsetY = { it }) }
        ) {
            val addPetViewModel: AddPetViewModel = hiltViewModel()
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                addPetViewModel.uiEffect.collect { effect ->
                    when (effect) {
                        is UiEffects.NavigateUp -> navController.navigateUp()
                        is UiEffects.ShowToastMessage -> {
                            Toast.makeText(context, effect.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            val state by addPetViewModel.state.collectAsState()
            AddPetScreen(
                state = state,
                navigateUp = { navController.navigateUp() },
                event = addPetViewModel::onEvent
            )
        }

        composable(
            route = Routes.PetDetailsScreen.route,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsVM: PetDetailsViewModel = hiltViewModel()

            val state by petDetailsVM.state.collectAsState()

            PetDetailsScreen(
                state = state,
                navigateUp = { navController.navigateUp() },
                onAddWeightClicked = {
                    petDetailsVM.addWeight(state.pet?.id, it)
                },
                onAddTaskClick = {
                    navController.navigate(Routes.AddTaskScreen.route)
                },
                onRangeChanged = {
                    petDetailsVM.updateWeightFilter(it)
                }
            )
        }
        composable(
            route = Routes.AddTaskScreen.route,
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
            enterTransition = { slideInVertically(initialOffsetY = { it }) }
        ) {
            val viewModel: AddTaskViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                viewModel.uiEffect.collect {
                    when (it) {
                        is AddTaskUiEffect.NavigateUp -> navController.navigateUp()
                        is AddTaskUiEffect.ShowSnackbar -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            AddTaskScreen(
                state = state,
                event = viewModel::onIntent,
                navigateUp = { navController.navigateUp() }
            )
        }

        composable(Routes.FeedbackScreen.route) {
            val viewModel: ProfileViewModel = hiltViewModel()

            val state by viewModel.state.collectAsStateWithLifecycle()
            FeedbackScreen(
                state = state,
                onSubmitClick = { viewModel.submitFeedback(it) },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable(Routes.EditUserScreen.route){

        }
    }
}