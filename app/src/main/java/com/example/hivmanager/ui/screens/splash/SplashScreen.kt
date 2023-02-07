package com.example.hivmanager.ui.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    onNavigate: (route: String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
){
    Column(modifier = Modifier.fillMaxSize().background(
        brush = Brush.linearGradient(
            listOf(
                Color(0xFF37BEFF),
                Color(0xFF43E97B),
            )
        )
    )) {

    }
}