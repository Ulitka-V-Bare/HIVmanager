package com.example.hivmanager.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.screens.info.InfoViewModel
import com.example.hivmanager.ui.theme.HIVmanagerTheme

@Composable
fun ChatScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: InfoViewModel = hiltViewModel()
) {
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
    ChatScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)}
    )
}


@Composable
private fun ChatScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {}
){
    Scaffold(
        topBar = { MyTopAppBar("Chat") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender,2) }
    ) {
        Column(modifier = Modifier.padding(it)) {

        }

    }
}

@Preview
@Composable
private fun ChatScreenPreview(){
    HIVmanagerTheme {
        ChatScreenUi()
    }
}