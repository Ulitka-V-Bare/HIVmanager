package com.example.hivmanager.ui.screens.diary

import androidx.lifecycle.ViewModel
import com.example.hivmanager.data.model.DiaryEntry
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.home.HomeEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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
        }
    }

    private fun addDiaryEntry(diaryEntry: DiaryEntry){

    }
}