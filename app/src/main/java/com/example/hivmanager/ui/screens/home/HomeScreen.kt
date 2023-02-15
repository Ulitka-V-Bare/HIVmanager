package com.example.hivmanager.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    fun openNotificationChannelSettings(context: Context) {
        val intent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
        //    .putExtra(Settings.EXTRA_CHANNEL_ID, Constants.CHANNEL_ID)
        context.startActivity(intent)
    }
    HomeScreenUi(
        bottomNavBarNavigationEventSender = { viewModel.sendNavigationEvent(it) },
        onOpenPillListClick = { viewModel.sendNavigationEvent(NavigationEvent.Navigate(Route.pillReminder)) },
        onOpenNotificationChannelSettingsClick = { openNotificationChannelSettings(context) },
        onConfirmEditHeightClick = {viewModel.onEvent(HomeEvent.OnConfirmEditHeightClick(it))},
        userHeight = viewModel.state.height
    )
}


@Composable
private fun HomeScreenUi(
    bottomNavBarNavigationEventSender: (NavigationEvent) -> Unit = {},
    onOpenPillListClick: () -> Unit = {},
    onOpenNotificationChannelSettingsClick: () -> Unit = {},
    userHeight: Int = 0,
    userAllergies: String = "",
    onConfirmEditAllergiesClick: (String)->Unit = {},
    onConfirmEditHeightClick: (String)->Unit = {},
) {
    Scaffold(
        topBar = { MyTopAppBar("Главная") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender, 1) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var isEditingHeight by remember { mutableStateOf(false) }
                Text(
                    text = "Мой рост: ",
                )
                if (!isEditingHeight) {
                    Text(
                        text = "$userHeight"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {isEditingHeight=true}) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "edit height button"
                        )
                    }
                } else {
                    var tempHeight by remember { mutableStateOf(if(userHeight==0) "" else userHeight.toString()) }
                    TextField(
                        value = tempHeight,
                        onValueChange = { value ->
                            if (value.length <= 3 && isAllDigits(value))
                                tempHeight = value
                        },
                        trailingIcon = {
                            Row() {
                                IconButton(onClick = { isEditingHeight = false }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "close height button"
                                    )
                                }
                                IconButton(onClick = {
                                    onConfirmEditHeightClick(tempHeight)
                                    isEditingHeight = false
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "close height button"
                                    )
                                }
                            }
                        }
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Мой Аллергии: $userAllergies",
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit height button"
                    )
                }
            }
            Button(onClick = onOpenPillListClick) {
                Text(text = "My pills")
            }
            Button(onClick = onOpenNotificationChannelSettingsClick) {
                Text(text = "Notification settings")
            }

        }
    }
}

private fun isAllDigits(s: String): Boolean {
    for (char in s) {
        if (!char.isDigit()) return false
    }
    return true
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HIVmanagerTheme {
        HomeScreenUi()
    }
}