package com.example.hivmanager.ui.screens.chat

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.R
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

val ONE_MEGABYTE: Long = 1024 * 1024
@HiltViewModel
class ChatViewModel  @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    @ApplicationContext
    val context: Context
    ): ViewModel() {

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
        viewModelScope.launch {
            startDownLoadingImages()
        }
    }

    suspend fun startDownLoadingImages(){
        Log.d("ChatViewModel","start downloading images")
        for(i in state.allMessages.size-1 downTo 0) {
            val message = state.allMessages[i]
            Log.d("ChatViewModel","${message.imageBitmap}")
            if(message.imageBitmap.isNotEmpty()){
                state = state.copy(
                    images = state.images.plus(message.imageBitmap to downloadImage(message.imageBitmap))
                )
                Log.d("ChatViewModel","downloaded image")
            }
        }
    }

    fun setImageUri(uri: Uri?){
        state = state.copy(imageUri = uri)
    }

    fun setImageBitmap(imageBitmap: ImageBitmap?){
        state = state.copy(imageBitmap = imageBitmap)
    }

    suspend fun downloadImage(imagePath:String):ImageBitmap?{
        try {
            val imageBytes = FirebaseStorage.getInstance().getReference(imagePath).getBytes(ONE_MEGABYTE).await()
            try {
                Log.d("ChatViewModel","try block")
                return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
            }catch (e:Exception){
                Log.d("ChatViewModel","${e.message}")
                return BitmapFactory.decodeResource(context.resources, R.drawable.image_not_available).asImageBitmap()
            }
        }catch (e:Exception){
            return BitmapFactory.decodeResource(context.resources, R.drawable.image_not_available).asImageBitmap()
        }
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
        if(state.message.isNotEmpty()||state.imageUri!=null) {
            userRepository.sendMessage(chatID, state.message, state.imageUri)
            state = state.copy(message = "", imageBitmap = null, imageUri = null)
        }
    }

    private fun onMessageValueChange(message:String){
        state = state.copy(message=message)
    }


    fun sendNavigationEvent(event: NavigationEvent){
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

    fun saveMediaToStorage(bitmap: Bitmap,imageName:String) {
        Log.d("ChatViewModel","")
        //Generating a file name
        val filename = "$imageName.png"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(context,"Изображение сохранено",Toast.LENGTH_SHORT).show()
        }
    }




    private fun getNewMessages(snapshot: DataSnapshot){
        val iterator = snapshot.children.iterator()
        while(iterator.hasNext()){
            val senderID = iterator.next().value
            val image = iterator.next().value
            val message = iterator.next().value
            val time = iterator.next().value
            val myMessage = Message(senderID.toString(),message.toString(),time.toString().toLong(), image.toString())
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