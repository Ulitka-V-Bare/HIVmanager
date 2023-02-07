package com.example.hivmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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

        }
        composable(
            route = Route.info
        ){

        }

    }
}