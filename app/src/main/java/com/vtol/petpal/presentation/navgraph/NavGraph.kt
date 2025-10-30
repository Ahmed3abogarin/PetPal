package com.vtol.petpal.presentation.navgraph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.R
import com.vtol.petpal.presentation.Routes
import com.vtol.petpal.presentation.home.HomeScreen
import com.vtol.petpal.presentation.navgraph.components.AppBottomNavComponent
import com.vtol.petpal.presentation.navgraph.components.BottomNavItem
import com.vtol.petpal.presentation.nearby.NearByScreen
import com.vtol.petpal.presentation.pets.PetsScreen
import com.vtol.petpal.presentation.profile.ProfileScreen


@Composable
fun AppNavigator() {
    val home = ImageVector.vectorResource(R.drawable.ic_home)
    val pets = ImageVector.vectorResource(R.drawable.ic_pets)
    val calender = ImageVector.vectorResource(R.drawable.ic_calender)
    val near = ImageVector.vectorResource(R.drawable.ic_nearby)
    val profile = ImageVector.vectorResource(R.drawable.ic_profile)


    val bottomItems = remember {
        mutableListOf(
            BottomNavItem(home, Color.Green, title = ""),
            BottomNavItem(pets, Color.Green, ""),
            BottomNavItem(calender, Color.Green, ""),
            BottomNavItem(near, Color.Green, ""),
            BottomNavItem(profile, Color.Green, ""),
        )
    }

    val navController = rememberNavController()
    val backstackState = navController.currentBackStackEntryAsState().value
    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    selectedItem = remember(key1 = backstackState) {
        when (backstackState?.destination?.route) {
            Routes.HomeScreen.route -> 0
            Routes.PetsScreen.route -> 1
            Routes.CalenderScreen.route -> 2
            Routes.NearbyScreen.route -> 3
            Routes.ProfileScreen.route -> 4
            else -> selectedItem
        }

    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            AppBottomNavComponent(
                selectedIndex = selectedItem,
                bottomItems = bottomItems,
                onItemClicked = { index ->
                    when (index) {
                        0 -> navigateToTab(
                            navController,
                            route = Routes.HomeScreen.route
                        )

                        1 -> navigateToTab(
                            navController,
                            route = Routes.PetsScreen.route
                        )

                        2 -> navigateToTab(
                            navController = navController,
                            route = Routes.CalenderScreen.route
                        )

                        3 -> navigateToTab(
                            navController = navController,
                            route = Routes.NearbyScreen.route
                        )

                        4 -> navigateToTab(
                            navController = navController,
                            route = Routes.ProfileScreen.route
                        )

                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    innerPadding
                ), startDestination = Routes.HomeScreen.route, navController = navController
        ) {
            composable(Routes.HomeScreen.route) {
                HomeScreen()
            }
            composable(Routes.PetsScreen.route) {
                PetsScreen()

            }
            composable(Routes.CalenderScreen.route) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Calender Screen")
                }
            }
            composable(
//                enterTransition = { slideInHorizontally(animationSpec = tween(400)) },
//                popExitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                route = Routes.NearbyScreen.route
            ) {
                NearByScreen()
            }
            composable(route = Routes.ProfileScreen.route) {
                ProfileScreen()
            }
        }

    }


}

fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
        }
        restoreState = true
        launchSingleTop = true
    }
}

//@Preview
//@Composable
//fun BottomNavigationPreview() {
//    val items = mutableListOf(
//        BottomNavItem("", Icons.Default.Home, Color.Black),
//        BottomNavItem("", Icons.Default.Home, Color.Black),
//        BottomNavItem("", Icons.Default.Home, Color.Black),
//    )
//    PetPalTheme {
//        MoviesBottomNav(items, selectedIndex = 0, onItemClicked = {})
//    }
//}


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.AppMainNavigation.route) {
        navigation(
            route = Routes.AppStartNavigation.route,
            startDestination = Routes.OnboardingScreen.route
        ) {
            composable(Routes.OnboardingScreen.route) {
                Box {
                    Text("gsgfd;jgfdjgfd;l")
                }
            }

        }
        navigation(
            route = Routes.AppMainNavigation.route,
            startDestination = Routes.ScreensNavigation.route
        ) {
            composable(Routes.ScreensNavigation.route) {
                AppNavigator()
            }
        }
    }
}