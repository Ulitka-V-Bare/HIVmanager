package com.example.hivmanager.ui.screens.doctorhome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.example.hivmanager.ui.screens.chat.ChatState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DoctorHomeViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository
):ViewModel() {

    var state by mutableStateOf(DoctorHomeState())
        private set

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()

    init {
        userRepository.loadLastMessages(viewModelScope) { patient, message ->
            addMessageToMap(
                patient,
                message
            )
        }
    }
    fun onSignOutClick(){
        viewModelScope.launch {
            userRepository.onSignOut()
            _navigationEvent.send(NavigationEvent.Navigate(Route.splash,true))
        }
    }
    private fun addMessageToMap(patient:String,message:String){
        state = state.copy(
            messages = state.messages.plus(patient to message)
        )
    }

    fun navigateToChat(userID:String){
        state = state.copy(patientID = userID)
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.Navigate(Route.doctorChat))
        }
    }

    fun navigateToInfo(userID:String){
        state = state.copy(patientID = userID)
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.Navigate(Route.userInfo))
        }
    }
}