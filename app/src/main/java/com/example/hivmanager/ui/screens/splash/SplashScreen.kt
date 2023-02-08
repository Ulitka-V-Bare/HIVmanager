package com.example.hivmanager.ui.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun SplashScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
){
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(SplashEvent.OnSplashScreenLaunched)
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route,it.popBackStack)
                }
                else -> {}
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(
        brush = Brush.linearGradient(
            listOf(
                Color(0xFF6097ff),
                Color(0xFF136acb),
                Color(0xFF004099),
            )
        )
    )) {
        //place for logo
    }
}