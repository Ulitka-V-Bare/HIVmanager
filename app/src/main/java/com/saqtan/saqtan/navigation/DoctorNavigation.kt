package com.saqtan.saqtan.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.saqtan.saqtan.ui.screens.chat.ChatScreen
import com.saqtan.saqtan.ui.screens.chat.ChatViewModel
import com.saqtan.saqtan.ui.screens.doctorhome.DoctorHomeScreen
import com.saqtan.saqtan.ui.screens.doctorhome.DoctorHomeViewModel
import com.saqtan.saqtan.ui.screens.userinfo.UserInfoScreen
import com.saqtan.saqtan.ui.screens.userinfo.UserInfoViewModel

private val doctorViewModelHolder = SharedViewModelHolder<DoctorHomeViewModel>()
private val chatViewModelHolder = SharedViewModelHolder<ChatViewModel>()
private val infoViewModelHolder = SharedViewModelHolder<UserInfoViewModel>()

/** навигация для врача
 * обычный пользователь может попасть в эти места через ссылку, но в приложении такие ссылки не предусмотрены
 * через Holder'ы передается нужная информация между viewModel
 * */
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

