package com.example.hivmanager.ui.screens.diary

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.model.DiaryEntry
import com.example.hivmanager.data.model.UserData
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.chat.ChatState
import com.example.hivmanager.ui.screens.home.HomeEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    //val channel: NotificationChannel
): ViewModel(){

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()



    fun onEvent(event: DiaryEvent) {
        when (event) {
            is DiaryEvent.OnAddDiaryEntryClick -> addDiaryEntry(event.diaryEntry)
            is DiaryEvent.OnDeleteDiaryEntryClick -> deleteDiaryEntry(event.diaryEntry)
        }
    }

    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

    private fun addDiaryEntry(diaryEntry: DiaryEntry){
        viewModelScope.launch {
            userRepository.addDiaryEntry(diaryEntry)
        }
    }

    private fun deleteDiaryEntry(diaryEntry: DiaryEntry){
        viewModelScope.launch {
            userRepository.deleteDiaryEntry(diaryEntry)
        }
    }
}