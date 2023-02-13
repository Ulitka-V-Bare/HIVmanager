package com.example.hivmanager.ui.screens.chat

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

data class ChatState(
    val message:String = "",
    val allMessages:List<Message> = listOf(),
    val isLoading:Boolean = true,
    val imageUri:Uri? = null,
    val imageBitmap: ImageBitmap? = null,
    val images: Map<String,ImageBitmap?> = mapOf()
)

data class Message(
    val sender:String,
    val text:String,
    val time:Long,
    val imageBitmap: String
)