package com.example.hivmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hivmanager.ui.screens.splash.SplashScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.splash
    ){
        composable(
            route = Route.chat
        ){

        }
        composable(
            route = Route.home
        ){

        }
        composable(
            route = Route.splash
        ){
            SplashScreen(
                onNavigate = navController::navigate
            )
        }
        composable(
            route = Route.info
        ){

        }

    }
}

fun NavHostController.navigate(
    route: String,
    popBackStack: Boolean = false
) {
    navigate(route) {
        if (popBackStack) {
            popUpTo(graph.id) {
                inclusive = true
            }
        }
    }
}