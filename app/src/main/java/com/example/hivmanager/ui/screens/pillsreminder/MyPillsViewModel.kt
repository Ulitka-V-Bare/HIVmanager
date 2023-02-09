package com.example.hivmanager.ui.screens.pillsreminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.model.PillInfo
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPillsViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,): ViewModel() {

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()

    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

    fun getPills(): List<PillInfo> {
        Log.d("MyPillsVM","${userRepository.getUserPills()}")
        return userRepository.getUserPills()
    }
}