package com.example.hivmanager.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.hivmanager.ui.screens.chat.ChatScreen
import com.example.hivmanager.ui.screens.chat.ChatViewModel
import com.example.hivmanager.ui.screens.doctorhome.DoctorHomeScreen
import com.example.hivmanager.ui.screens.doctorhome.DoctorHomeViewModel
import com.example.hivmanager.ui.screens.userinfo.UserInfoScreen
import com.example.hivmanager.ui.screens.userinfo.UserInfoViewModel

private val doctorViewModelHolder = SharedViewModelHolder<DoctorHomeViewModel>()
private val chatViewModelHolder = SharedViewModelHolder<ChatViewModel>()
private val infoViewModelHolder = SharedViewModelHolder<UserInfoViewModel>()
fun NavGraphBuilder.doctorNavigation(
    navController: NavHostController
) {
    navigation(
        startDestination = Route.doctorHome,
        route = "doctorNavigation"
    ) {
        composable(
            route = Route.doctorHome
        ) {
            DoctorHomeScreen(
                onNavigate = navController::navigate,
                viewModel = doctorViewModelHolder.createNewSharedViewModel()
            )
        }
        composable(
            Route.doctorChat
        ) {
            ChatScreen(
                onNavigate = navController::navigate,
                viewModel = chatViewModelHolder.createNewSharedViewModel().let {
                    it.patientID = doctorViewModelHolder.getSharedViewModel().state.patientID
                    it
                },
                onNavigateUp = { navController.navigateUp() },
                isDoctor = true
            )
        }
        composable(
            route = Route.userInfo
        ) {
            UserInfoScreen(
                onNavigate = navController::navigate,
                onNavigateUp = { navController.navigateUp() },
                viewModel = infoViewModelHolder.createNewSharedViewModel().let {
                    it.patientID = doctorViewModelHolder.getSharedViewModel().state.patientID ?: ""
                    it
                }
            )
        }
    }
}

