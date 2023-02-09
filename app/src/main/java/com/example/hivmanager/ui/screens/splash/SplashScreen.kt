package com.example.hivmanager.ui.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.R
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

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            MaterialTheme.colors.background
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image (
            modifier = Modifier
                .width(300.dp)
                .height(350.dp),
            painter = painterResource(id = if(isSystemInDarkTheme())R.drawable.logo_light else R.drawable.logo_no_background),
            contentDescription = "Big logo"
        )
    }
}