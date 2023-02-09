package com.example.hivmanager.ui.screens.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.screens.home.HomeViewModel
import com.example.hivmanager.ui.theme.HIVmanagerTheme

@Composable
fun InfoScreen(
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
    InfoScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)}
    )
}


@Composable
private fun InfoScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {}
){
    Scaffold(
        topBar = { MyTopAppBar("Info") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender,0) }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            //place fo info
        }

    }
}

@Preview
@Composable
private fun InfoScreenPreview(){
    HIVmanagerTheme {
        InfoScreenUi()
    }
}