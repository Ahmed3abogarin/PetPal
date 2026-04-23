package com.vtol.petpal.presentation.navgraph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.R
import com.vtol.petpal.presentation.navgraph.components.AppBottomNavComponent
import com.vtol.petpal.presentation.navgraph.components.BottomNavItem
import com.vtol.petpal.ui.theme.BackgroundColor


@Composable
fun MainScreen() {

    val navController = rememberNavController()

    val bottomItems = remember {
        mutableListOf(
            BottomNavItem(
                defaultIcon = R.drawable.ic_home_outlined,
                filledIcon = R.drawable.ic_home_filled,
                title = "Home"
            ),
            BottomNavItem(
                defaultIcon = R.drawable.ic_pets_outlined,
                filledIcon = R.drawable.ic_pets_filled,
                title = "Pets"
            ),
            BottomNavItem(
                defaultIcon = R.drawable.ic_calendar_outlined,
                filledIcon = R.drawable.ic_calendar_filled,
                title = "Calendar"
            ),
            BottomNavItem(
                defaultIcon = R.drawable.ic_world_outlined,
                filledIcon = R.drawable.ic_world_filled,
                title = "Explore"
            ),
            BottomNavItem(
                defaultIcon = R.drawable.ic_user_outlined,
                filledIcon = R.drawable.ic_user_filled,
                title = "Profile"
            )
        )
    }
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

    val isBottomNavVisible = remember(backstackState) {
        backstackState?.destination?.route == Routes.HomeScreen.route ||
                backstackState?.destination?.route == Routes.PetsScreen.route ||
                backstackState?.destination?.route == Routes.CalenderScreen.route ||
                backstackState?.destination?.route == Routes.NearbyScreen.route ||
                backstackState?.destination?.route == Routes.ProfileScreen.route
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomNavVisible,
                exit = slideOutVertically (animationSpec = tween(200)){ it },
                enter = slideInVertically { it }
            ) {
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

        }
    ) { p ->
        NavHost(
            navController = navController,
            startDestination = Routes.MainGraph.route,
            modifier = Modifier.padding(bottom = p.calculateBottomPadding())
        ) {
            mainNavGraph(navController)
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
