package com.example.hivmanager.ui.screens.pillsreminder

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.model.PillInfo
import com.example.hivmanager.data.model.UserData
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.data.repository.dataStore
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.example.hivmanager.ui.screens.addpill.AddPillEvent
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



    fun onEvent(event: MyPillsEvent) {
        when (event) {
            MyPillsEvent.OnAddNewPillInfoClick ->onAddNewPillInfoClick()
            is MyPillsEvent.OnDeletePillInfoClick ->onDeletePillInfoClick(event.index)
        }
    }

    private fun onDeletePillInfoClick(index:Int){
        viewModelScope.launch {
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

    fun getPills(): List<PillInfo> {
        Log.d("MyPillsVM","${userRepository.getUserPills()}")
        return userRepository.getUserPills()
    }
}