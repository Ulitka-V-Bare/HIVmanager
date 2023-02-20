package com.saqtan.saqtan.ui.screens.doctorhome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.saqtan.saqtan.data.repository.UserRepository
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.navigation.Route
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

    /***
     * при инициализации запрашиваем из базы последние сообщения всех чатов
     */
    init {
        userRepository.loadLastMessages(viewModelScope) { patient, message ->
            addMessageToMap(
                patient,
                message
            )
        }
    }
    /***
     * выход из аккаунта
     */
    fun onSignOutClick(){
        viewModelScope.launch {
            userRepository.onSignOut()
            _navigationEvent.send(NavigationEvent.Navigate(Route.splash,true))
        }
    }
    /***
     * добавляет сообщение в Map
     */
    private fun addMessageToMap(patient:String,message:String){
        state = state.copy(
            messages = state.messages.plus(patient to message)
        )
    }
    /***
     * перенаправление в чат с пользователем
     */
    fun navigateToChat(userID:String){
        state = state.copy(patientID = userID)
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.Navigate(Route.doctorChat))
        }
    }
    /***
     * перенаправление в информацию о пользователе
     */
    fun navigateToInfo(userID:String){
        state = state.copy(patientID = userID)
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent.Navigate(Route.userInfo))
        }
    }
}