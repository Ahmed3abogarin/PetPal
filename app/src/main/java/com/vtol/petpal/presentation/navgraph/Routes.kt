package com.vtol.petpal.presentation.navgraph


sealed class Routes(val route: String) {

    data object OnboardingScreen: Routes("onboardingScreen")
    data object SplashScreen: Routes("splashScreen")

    data object ErrorScreen: Routes("errorScreen")



    // app main screens

    data object LoginScreen: Routes("loginScreen")
    data object SignUpScreen: Routes("signUpScreen")
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

    data object FeedbackScreen: Routes("feedbackScreen")


    data object MainGraph: Routes("mainGraph")

    data object MainScreen: Routes("mainScreen")
    data object AuthGraph: Routes("authGraph")


}