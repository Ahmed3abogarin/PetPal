package com.vtol.petpal.presentation

sealed class Routes(val route: String) {

    // app main screens
    data object OnboardingScreen: Routes("onboardingScreen")
    data object HomeScreen: Routes("homeScreen")
    data object PetsScreen: Routes("petsScreen")
    data object CalenderScreen: Routes("calenderScreen")
    data object NearbyScreen: Routes("nearbyScreen")
    data object ProfileScreen: Routes("profileScreen")

    // sub screens
    data object AddPetScreen: Routes("addPetScreen")
    data object PetDetailsScreen : Routes("petDetailsScreen/{petId}") {
        fun createRoute(petId: String) = "petDetailsScreen/$petId"
    }
    data object AddTaskScreen: Routes("addTaskScreen")


    // app navigation
    data object AppStartNavigation: Routes("appStartNavigation")
    data object AppMainNavigation: Routes("appMainNavigation")
    data object ScreensNavigation: Routes("screensNavigation")


}