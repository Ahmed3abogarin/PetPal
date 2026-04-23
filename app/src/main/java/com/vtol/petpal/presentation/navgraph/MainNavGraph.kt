package com.vtol.petpal.presentation.navgraph

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.vtol.petpal.presentation.calender.CalenderScreen
import com.vtol.petpal.presentation.calender.CalenderViewModel
import com.vtol.petpal.presentation.common.UserViewModel
import com.vtol.petpal.presentation.explore.NearByScreen
import com.vtol.petpal.presentation.home.AddTaskScreen
import com.vtol.petpal.presentation.home.HomeScreen
import com.vtol.petpal.presentation.home.HomeViewModel
import com.vtol.petpal.presentation.pets.AddPetScreen
import com.vtol.petpal.presentation.pets.PetDetailsScreen
import com.vtol.petpal.presentation.pets.PetDetailsViewModel
import com.vtol.petpal.presentation.pets.PetViewModel
import com.vtol.petpal.presentation.pets.PetsScreen
import com.vtol.petpal.presentation.profile.FeedbackScreen
import com.vtol.petpal.presentation.profile.ProfileScreen
import com.vtol.petpal.presentation.profile.ProfileViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavController){
    navigation(
        startDestination = Routes.HomeScreen.route,
        route = Routes.MainGraph.route
    ) {

        composable(Routes.HomeScreen.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val userViewModel: UserViewModel = hiltViewModel()

            HomeScreen(
                onAddTaskClicked = {
                    navController.navigate(Routes.AddTaskScreen.route)
                },
                onAddPetClicked = {
                    navController.navigate(Routes.AddPetScreen.route)
                },
                viewModel = homeViewModel,
                onPetClicked = {
                    navController.navigate(Routes.PetDetailsScreen.createRoute(it)) {
                        launchSingleTop = false
                    }
                },
                userViewModel = userViewModel
            )
        }
        composable(Routes.PetsScreen.route) {

            PetsScreen(
                viewModel = hiltViewModel<PetViewModel>(),
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
            NearByScreen()
        }
        composable(route = Routes.ProfileScreen.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()


            ProfileScreen(
                user = hiltViewModel<UserViewModel>().state.collectAsState().value,
                state = profileViewModel.uiState.collectAsState().value,
                event = profileViewModel::onEvent,
                petsCount = hiltViewModel<PetViewModel>().state.collectAsState().value.pets.size,
                doneTasks = hiltViewModel<HomeViewModel>().state.collectAsState().value.completedCount,
                navigateToFeedBack = {
                    navController.navigate(Routes.FeedbackScreen.route)
                }
            )
        }

        // sub screens
        composable(route = Routes.AddPetScreen.route) {
            val addPetViewM: PetViewModel = hiltViewModel()
            AddPetScreen(addPetViewM, navigateUp = { navController.navigateUp() })
        }

        composable(
            route = Routes.PetDetailsScreen.route,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            val petDetailsVM: PetDetailsViewModel = hiltViewModel()

            PetDetailsScreen(petViewModel = petDetailsVM) { navController.navigateUp() }
        }
        composable(Routes.AddTaskScreen.route) {
            val pets = hiltViewModel<PetViewModel>().state.collectAsState()
            AddTaskScreen(
                viewModel = hiltViewModel<HomeViewModel>(),
                petsList = pets.value.pets,
                navigateUp = { navController.navigateUp() }
            )
        }


        composable(Routes.FeedbackScreen.route) {

            FeedbackScreen(
                viewModel = hiltViewModel<ProfileViewModel>(),
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}