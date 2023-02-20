package com.saqtan.saqtan.ui.screens.pillsreminder

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saqtan.saqtan.data.model.notification.NotificationHelper
import com.google.firebase.auth.FirebaseAuth
import com.saqtan.saqtan.data.model.notification.AlarmScheduler
import com.saqtan.saqtan.data.repository.UserRepository
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPillsViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    val scheduler: AlarmScheduler,
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
    /***
     * удаление записи о напоминании и отключение всех запланированных уведомлений, связанных с ним
     */
    private fun onDeletePillInfoClick(index:Int){
        viewModelScope.launch {
            Log.d("cancelPill","before cancelNotification")
            notificationHelper.cancelNotifications(userRepository.loadUserLocalData().pillInfoList[index],scheduler)
            Log.d("cancelPill","before deletePillInfo")
            userRepository.deletePillInfo(index)
        }
    }
    /***
     * переход на страницу добавления напоминания
     */

    private fun onAddNewPillInfoClick(){
        sendNavigationEvent(NavigationEvent.Navigate(Route.addPill))
    }
    /***
     * отправка события навигации
     */
    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}