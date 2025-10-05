package com.vtol.petpal.presentation

sealed class Routes(val route: String) {

    // app screens
    data object OnboardingScreen: Routes("onboardingScreen")
    data object HomeScreen: Routes("homeScreen")
    data object PetsScreen: Routes("petsScreen")
    data object CalenderScreen: Routes("calenderScreen")
    data object NearbyScreen: Routes("nearbyScreen")
    data object ProfileScreen: Routes("profileScreen")


    // app navigation
    data object AppStartNavigation: Routes("appStartNavigation")
    data object AppMainNavigation: Routes("appMainNavigation")
    data object ScreensNavigation: Routes("screensNavigation")


}