package com.saqtan.saqtan.ui.screens.userinfo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saqtan.saqtan.data.model.UserData
import com.saqtan.saqtan.data.repository.UserRepository
import com.saqtan.saqtan.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    userRepository: UserRepository
):ViewModel() {
    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()

    var patientID:String = ""

    var userData by mutableStateOf(UserData())
        private set

    /** при инициализации загрузка данных нужного пользователя из базы,
     * delay нужен, чтобы данные patientID из графа навигации, так как это происходит не мгновенно
     * сначала инициализируется без данных об айди, затем начинается delay в 50 миллисекунд, в течение которого
     * должны прийти данные
     */
    init{
        viewModelScope.launch {
            while(patientID=="") {
                delay(50)
            }
            userData = userRepository.loadUserDataFromDatabase(patientID)
        }
    }

    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

}