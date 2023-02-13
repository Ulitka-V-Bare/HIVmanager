package com.example.hivmanager.ui.screens.chat

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
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

    var patientID:String? = null

    var chatID = ""
    init {
        viewModelScope.launch {
            delay(100)
            chatID = if(patientID==null) "${auth.uid}${userRepository.userDoctorID}" else "${patientID}${auth.uid}"
            userRepository.getMessageList( chatID,{ onGetDate(it) })
        }
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
            chatID = chatID,
            onChildAddedListener = {getNewMessages(it)},
            onLoaded = {}
        )
    }

    fun setImageUri(uri: Uri?){
        state = state.copy(imageUri = uri)
    }

    fun setImageBitmap(imageBitmap: ImageBitmap?){
        state = state.copy(imageBitmap = imageBitmap)
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
        userRepository.sendMessage(chatID,state.message)
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