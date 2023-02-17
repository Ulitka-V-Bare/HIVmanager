package com.example.hivmanager.ui.screens.pillsreminder

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.model.PillInfo
import com.example.hivmanager.data.model.UserData
import com.example.hivmanager.data.model.notification.AlarmItem
import com.example.hivmanager.data.model.notification.AlarmScheduler
import com.example.hivmanager.data.model.notification.AndroidAlarmScheduler
import com.example.hivmanager.data.model.notification.NotificationHelper
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.data.repository.dataStore
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.example.hivmanager.ui.screens.addpill.AddPillEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MyPillsViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    val scheduler:AlarmScheduler,
    val notificationHelper: NotificationHelper
    ): ViewModel() {

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()



    fun onEvent(event: MyPillsEvent) {
        when (event) {
            MyPillsEvent.OnAddNewPillInfoClick ->onAddNewPillInfoClick()
            is MyPillsEvent.OnDeletePillInfoClick ->onDeletePillInfoClick(event.index)
        }
    }

    private fun onDeletePillInfoClick(index:Int){
        viewModelScope.launch {
            Log.d("cancelPill","before cancelNotification")
            notificationHelper.cancelNotifications(userRepository.loadUserLocalData().pillInfoList[index],scheduler)
            Log.d("cancelPill","before deletePillInfo")
            userRepository.deletePillInfo(index)
        }
    }


    private fun onAddNewPillInfoClick(){
        sendNavigationEvent(NavigationEvent.Navigate(Route.addPill))
    }
    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}