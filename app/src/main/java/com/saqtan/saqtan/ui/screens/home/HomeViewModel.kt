package com.saqtan.saqtan.ui.screens.home

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
class HomeViewModel  @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    //val channel: NotificationChannel
    ):ViewModel() {

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()

    /***
     * обработка внешнего события
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnConfirmEditAllergiesClick -> onConfirmEditAllergiesClick(event.allergies)
            is HomeEvent.OnConfirmEditHeightClick -> onConfirmEditHeightClick(event.height)
            HomeEvent.OnSignOutClick -> onSignOutClick()
        }
    }
    /***
     * выход из аккаунта
     */
    private fun onSignOutClick(){
        viewModelScope.launch {
            userRepository.onSignOut()
            _navigationEvent.send(NavigationEvent.Navigate(Route.splash,true))
        }
    }
    /***
     * сохраняем рост локально и в базу
     */

    private fun onConfirmEditHeightClick(height:String){
        viewModelScope.launch {
            userRepository.setHeight(if(height.isEmpty()) 0 else height.toInt())
        }
    }

    /***
     * сохраняем аллергии локально и в базу
     */
    private fun onConfirmEditAllergiesClick(allergies:String){
//        state = state.copy(
//            allergies = allergies
//        )
        viewModelScope.launch {
            userRepository.setAllergies(allergies)
        }
    }
    /***
     * отправка события навигации
     */
    fun sendNavigationEvent(event:NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}