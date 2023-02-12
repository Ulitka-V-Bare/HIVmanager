package com.example.hivmanager.ui.screens.home

import android.app.NotificationChannel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.model.notification.NotificationHelper
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.signin.SignInState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    //val channel: NotificationChannel
    ):ViewModel() {

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()



    fun sendNavigationEvent(event:NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}