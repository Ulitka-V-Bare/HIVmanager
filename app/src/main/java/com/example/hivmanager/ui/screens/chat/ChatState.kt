package com.example.hivmanager.ui.screens.chat

data class ChatState(
    val message:String = "",
    val allMessages:List<Message> = listOf()
)

data class Message(
    val sender:String,
    val text:String,
    val time:Long
)