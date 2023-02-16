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
    val scheduler:AlarmScheduler
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
            cancelNotifications(userRepository.loadUserLocalData().pillInfoList[index])
            Log.d("cancelPill","before deletePillInfo")
            userRepository.deletePillInfo(index)
        }
    }

    private fun cancelNotifications(pillInfo: PillInfo){
        for(time in pillInfo.timeToTakePill){
            Log.d("cancelPill","${pillInfo.startDate} - ${time}")
            val year = pillInfo.startDate.split('.')[2].toInt()
            val month =pillInfo.startDate.split('.')[1].toInt()
            val day = pillInfo.startDate.split('.')[0].toInt()
            val hour = time.split(':')[0].toInt()
            val minute = time.split(':')[1].toInt()
            val firstDayToNotify = LocalDateTime.of(
                year,
                month,
                day,
                hour,
                minute,
                0,
                0
            )
            Log.d("cancelPill","$firstDayToNotify")
            //val firstDayToNotify = LocalDateTime.now().withHour(time.split(':')[0].toInt()).withMinute(time.split(':')[1].toInt())
            for(i in 0 until pillInfo.duration){
                val alarmItem = AlarmItem(firstDayToNotify.plusDays(i.toLong()),"время принять: ${pillInfo.name}")
                alarmItem.let(scheduler::cancel)
            }
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