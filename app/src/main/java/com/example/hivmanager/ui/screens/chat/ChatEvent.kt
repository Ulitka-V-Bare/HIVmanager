package com.example.hivmanager.ui.screens.chat

/** события чата
 * */
sealed class ChatEvent {
    object OnSendMessageButtonClick:ChatEvent()
    data class OnMessageValueChange(val message:String):ChatEvent()

    object OnReloadClick:ChatEvent()
}