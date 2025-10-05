package com.vtol.petpal.presentation.navgraph

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.vtol.petpal.R
import com.vtol.petpal.presentation.Routes
import com.vtol.petpal.presentation.home.HomeScreen
import com.vtol.petpal.presentation.pets.PetsScreen
import com.vtol.petpal.presentation.profile.ProfileScreen
import com.vtol.petpal.ui.theme.LightPurple
import com.vtol.petpal.ui.theme.MainPurple
import kotlinx.coroutines.delay

@Composable
fun MoviesBottomNav(
    bottomItems: MutableList<BottomNavItem>,
    selectedIndex: Int,
    onItemClicked: (Int) -> Unit,
) {
    val switching = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(switching.value) {
        if (switching.value) {
            delay(250)
            switching.value = false
        }
    }


    // The row that contains all the bottom icons
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)).shadow(8.dp)
            .background(Color.White)

    ) {


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomItems.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                val iconOffset by animateDpAsState(
                    targetValue = if (isSelected) 6.dp else 0.dp, // how much it moves down
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                )

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.1f else 1f,
                    animationSpec = tween(300, easing = LinearOutSlowInEasing),
                    label = ""
                )
                val interactionSource = remember { MutableInteractionSource() }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.clickable (
                        indication = null,
                        interactionSource =  interactionSource
                    ){
                        onItemClicked(index)
                    }
                ) {
                    AnimatedVisibility(
                        visible = isSelected,
                        enter = fadeIn(tween(300)),
                        exit = fadeOut(tween(300))
                    ) {
                        Box(
                            modifier = Modifier
                                .height(15.dp)
                                .width(25.dp)
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                                .background(MainPurple)
                        )
                    }


                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (isSelected) LightPurple else Color.Gray,
                        modifier = Modifier
                            .size(36.dp)
                            .offset(y = iconOffset)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale
                            )
                    )
                    Text(
                        text = item.title,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }

}

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(startDestination = Routes.HomeScreen.route, navController = navController) {
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nearby Screen")
                }
            }
            composable(route = Routes.ProfileScreen.route) {
                ProfileScreen()
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .shadow(3.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        ) {
            MoviesBottomNav(
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

data class BottomNavItem(
    val icon: ImageVector,
    val color: Color,
    val title: String,
    val offset: Offset = Offset(0f, 0f),
    val size: IntSize = IntSize(10, 10),
)
