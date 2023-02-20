package com.saqtan.saqtan.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.saqtan.saqtan.ui.screens.addpill.AddPillScreen
import com.saqtan.saqtan.ui.screens.chat.ChatScreen
import com.saqtan.saqtan.ui.screens.diary.DiaryScreen
import com.saqtan.saqtan.ui.screens.home.HomeScreen
import com.saqtan.saqtan.ui.screens.info.InfoScreen
import com.saqtan.saqtan.ui.screens.pillsreminder.MyPillsScreen
import com.saqtan.saqtan.ui.screens.signin.SignInScreen
import com.saqtan.saqtan.ui.screens.signin.SignInScreenCodeSent
import com.saqtan.saqtan.ui.screens.signin.SignInViewModel
import com.saqtan.saqtan.ui.screens.splash.SplashScreen

private val viewModelHolder = SharedViewModelHolder<SignInViewModel>()

/** граф навигации, signIn и signInCodeSent используют одну viewModel
 * */
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
            ChatScreen(onNavigate = navController::navigate,
                onNavigateUp = {navController.navigateUp()},)
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
            AddPillScreen(onNavigate = navController::navigate,
                onNavigateUp = {navController.navigateUp()},)
        }
        composable(
            route = Route.pillReminder
        ){
            MyPillsScreen(onNavigate = navController::navigate,
                onNavigateUp = {navController.navigateUp()},)
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