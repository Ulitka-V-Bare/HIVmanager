package com.saqtan.saqtan.ui.screens.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.saqtan.saqtan.data.model.DiaryEntry
import com.saqtan.saqtan.data.repository.UserRepository
import com.saqtan.saqtan.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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


    /***
     * обработка внешнего события
     */
    fun onEvent(event: DiaryEvent) {
        when (event) {
            is DiaryEvent.OnAddDiaryEntryClick -> addDiaryEntry(event.diaryEntry)
            is DiaryEvent.OnDeleteDiaryEntryClick -> deleteDiaryEntry(event.diaryEntry)
        }
    }
    /***
     * отправление события навигации
     */
    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
    /***
     * добавление записи в дневник
     */
    private fun addDiaryEntry(diaryEntry: DiaryEntry){
        viewModelScope.launch {
            userRepository.addDiaryEntry(diaryEntry)
        }
    }
    /***
     * удаление записи из дневника
     */
    private fun deleteDiaryEntry(diaryEntry: DiaryEntry){
        viewModelScope.launch {
            userRepository.deleteDiaryEntry(diaryEntry)
        }
    }
}