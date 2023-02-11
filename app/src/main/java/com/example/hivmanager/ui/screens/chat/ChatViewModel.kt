package com.example.hivmanager.ui.screens.chat

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import kotlinx.coroutines.delay
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

    val lazyColumnScrollState = LazyListState()



    init {
//        viewModelScope.launch {
//            state=state.copy(
//                allMessages = userRepository.getMessageList("testmessages").sortedBy { it.time }
//            )
//        }
        viewModelScope.launch {
            userRepository.getMessageList("testmessages", { onGetDate(it) })
        }
//        userRepository.setOnUpdateListener(
//            onChildAddedListener = {getNewMessages(it)},
//            onLoaded = {changeStateToLoaded()}
//        )
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageValueChange -> onMessageValueChange(event.message)
            ChatEvent.OnSendMessageButtonClick -> onSendMessageButtonClick()
        }
    }

    private fun onGetDate(list:MutableList<Message>){
        state = state.copy(
            allMessages = list.sortedBy { it.time },
            isLoading = false
        )
        Log.d("ChatViewModel","chat loaded")
        userRepository.setOnUpdateListener(
            onChildAddedListener = {getNewMessages(it)},
            onLoaded = {}
        )
    }

    private fun changeStateToLoaded(){

        if(state.isLoading){
            state=state.copy(
                allMessages = state.allMessages.sortedBy { it.time },
                isLoading = false
            )
            Log.d("ChatViewModel","chat loaded")
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
            val myMessage = Message(senderID.toString(),message.toString(),time.toString().toLong())
            if(myMessage !in state.allMessages) {
                state = state.copy(
                    allMessages = state.allMessages.plus(myMessage)
                )
                viewModelScope.launch {
                    delay(100)
                    lazyColumnScrollState.scrollToItem(state.allMessages.size)
                }
            }
            Log.d("ChatViewModel","$message")
        }
    }
}