package com.example.hivmanager.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hivmanager.ui.screens.addpill.AddPillScreen
import com.example.hivmanager.ui.screens.chat.ChatScreen
import com.example.hivmanager.ui.screens.diary.DiaryScreen
import com.example.hivmanager.ui.screens.doctorhome.DoctorHomeScreen
import com.example.hivmanager.ui.screens.home.HomeScreen
import com.example.hivmanager.ui.screens.info.InfoScreen
import com.example.hivmanager.ui.screens.pillsreminder.MyPillsScreen
import com.example.hivmanager.ui.screens.signin.SignInScreen
import com.example.hivmanager.ui.screens.signin.SignInScreenCodeSent
import com.example.hivmanager.ui.screens.signin.SignInViewModel
import com.example.hivmanager.ui.screens.splash.SplashScreen

private val viewModelHolder = SharedViewModelHolder<SignInViewModel>()
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
            ChatScreen(onNavigate = navController::navigate)
        }
        composable(
            route = Route.home
        ){
            HomeScreen(onNavigate = navController::navigate)
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
            InfoScreen(onNavigate = navController::navigate)
        }
        composable(
            route = Route.signIn
        ){
            SignInScreen(
                viewModel = viewModelHolder.createNewSharedViewModel(),
                onNavigate = navController::navigate
            )
        }
        composable(
            route = Route.signInCodeSent
        ){
            SignInScreenCodeSent(
                onNavigate = navController::navigate,
                viewModel = viewModelHolder.getSharedViewModel(),
                onNavigateUp = {navController.navigateUp()}
            )
        }
        composable(
            route = Route.addPill
        ){
            AddPillScreen(onNavigate = navController::navigate)
        }
        composable(
            route = Route.pillReminder
        ){
            MyPillsScreen(onNavigate = navController::navigate)
        }
        composable(
            route=Route.diary
        ){
            DiaryScreen(
                onNavigate = navController::navigate,
                onNavigateUp = {navController.navigateUp()},
            )
        }
        doctorNavigation(navController)
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