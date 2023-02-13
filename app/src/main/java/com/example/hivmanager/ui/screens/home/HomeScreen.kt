package com.example.hivmanager.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.data.model.Constants
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.theme.HIVmanagerTheme


@Composable
fun HomeScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route, it.popBackStack)
                }
                else -> {}
            }
        }
    }
    fun openNotificationChannelSettings(context:Context){
        val intent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
        //    .putExtra(Settings.EXTRA_CHANNEL_ID, Constants.CHANNEL_ID)
        context.startActivity(intent)
    }
    HomeScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)},
        onOpenPillListClick = {viewModel.sendNavigationEvent(NavigationEvent.Navigate(Route.pillReminder))},
        onOpenNotificationChannelSettingsClick = {openNotificationChannelSettings(context)}
    )
}


@Composable
private fun HomeScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {},
    onOpenPillListClick:()->Unit={},
    onOpenNotificationChannelSettingsClick:()->Unit = {}
){
    Scaffold(
        topBar = {MyTopAppBar("Главная")},
        bottomBar = {BottomNavBar(bottomNavBarNavigationEventSender,1)}
    ) {
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = onOpenPillListClick){
                Text(text = "My pills")
            }
            Button(onClick = onOpenNotificationChannelSettingsClick) {
                Text(text = "Notification settings")
            }
            
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview(){
    HIVmanagerTheme {
        HomeScreenUi()
    }
}