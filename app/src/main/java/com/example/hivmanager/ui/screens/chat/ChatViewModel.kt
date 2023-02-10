package com.example.hivmanager.ui.screens.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel  @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,): ViewModel() {

    var state by mutableStateOf(ChatState())
        private set

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()



    init {
//        viewModelScope.launch {
//            state=state.copy(
//                allMessages = userRepository.getMessageList("testmessages").sortedBy { it.time }
//            )
//        }
        userRepository.setOnUpdateListener {
            getNewMessages(it)
        }
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageValueChange -> onMessageValueChange(event.message)
            ChatEvent.OnSendMessageButtonClick -> onSendMessageButtonClick()
        }
    }

    private fun onSendMessageButtonClick(){
        userRepository.sendMessage(state.message)
        state=state.copy(message = "")
    }

    private fun onMessageValueChange(message:String){
        state = state.copy(message=message)
    }


    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }


    private fun getNewMessages(snapshot: DataSnapshot){
        val iterator = snapshot.children.iterator()
        while(iterator.hasNext()){
            val senderID = iterator.next().value
            val message = iterator.next().value
            val time = iterator.next().value
            state=state.copy(allMessages = state.allMessages.plus(Message(senderID.toString(),message.toString(),time.toString().toLong())))
            Log.d("ChatViewModel","$message")
        }
    }
}