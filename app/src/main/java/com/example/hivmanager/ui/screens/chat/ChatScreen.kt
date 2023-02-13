package com.example.hivmanager.ui.screens.chat

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.ImageContainer
import com.example.hivmanager.ui.screens.components.LoadingGif
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.screens.info.InfoViewModel
import com.example.hivmanager.ui.theme.HIVmanagerTheme
import com.example.hivmanager.ui.theme.White200
import com.example.hivmanager.ui.theme.White500
import kotlinx.coroutines.NonDisposableHandle.parent

@Composable
fun ChatScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
    isDoctor:Boolean = false
) {
    val activity = (LocalContext.current as Activity)


    LaunchedEffect(key1 = true) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route, it.popBackStack)
                }
                else -> {}
            }
        }
    }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.d("photo","launcher entered")
        viewModel.setImageUri(uri)
        var bitmap: Bitmap? = null
        viewModel.state.imageUri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
        viewModel.setImageBitmap(bitmap?.asImageBitmap())
    }


    if(viewModel.userRepository.userDoctorID!="null" || viewModel.userRepository.userType=="doctor")
        ChatScreenUi(
            bottomNavBarNavigationEventSender = { viewModel.sendNavigationEvent(it) },
            textFieldValue = viewModel.state.message,
            onTextFieldValueChange = { viewModel.onEvent(ChatEvent.OnMessageValueChange(it)) },
            onSendMessageButtonClick = { viewModel.onEvent(ChatEvent.OnSendMessageButtonClick) },
            messageList = viewModel.state.allMessages,
            userID = viewModel.auth.uid,
            lazyListState = viewModel.lazyColumnScrollState,
            isLoading = viewModel.state.isLoading,
            isDoctor = isDoctor,
            onAddImageClick = { launcher.launch("image/*") },
            imageBitmap = viewModel.state.imageBitmap,
            onDeleteImageClick = {
                viewModel.setImageBitmap(null)
                viewModel.setImageUri(null)
            }
        )
    else{
        ChatNowAvailableUi(bottomNavBarNavigationEventSender = { viewModel.sendNavigationEvent(it) })
    }
}

@Composable
private fun ChatNowAvailableUi(
    bottomNavBarNavigationEventSender: (NavigationEvent) -> Unit = {},
){
    Scaffold(
        topBar = { MyTopAppBar("Чат")},
        bottomBar = {
                BottomNavBar(bottomNavBarNavigationEventSender, 2)
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize(),
        contentAlignment = Alignment.Center) {
            Text(
                text = "Мы еще не прикрепили вас ко врачу, обратитесь по адресу",
                modifier = Modifier.width(200.dp),
            textAlign = TextAlign.Center,
                fontSize = 20.sp
                )
        }
    }
}


@Composable
private fun ChatScreenUi(
    bottomNavBarNavigationEventSender: (NavigationEvent) -> Unit = {},
    textFieldValue: String = "",
    onTextFieldValueChange: (String) -> Unit = {},
    onSendMessageButtonClick: () -> Unit = {},
    messageList: List<Message> = listOf(),
    userID: String? = "",
    lazyListState: LazyListState = rememberLazyListState(),
    isLoading:Boolean = false,
    isDoctor: Boolean = false,
    onAddImageClick:()->Unit = {},
    onDeleteImageClick:()->Unit = {},
    imageBitmap:ImageBitmap? = null
) {
    Scaffold(
        topBar = { MyTopAppBar("Чат")},
        bottomBar = {
            Column() {
                if(imageBitmap!=null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        ImageContainer(imageBitmap = imageBitmap, onCloseClick = onDeleteImageClick,
                            modifier = Modifier.padding(4.dp).size(76.dp))
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = textFieldValue,
                        onValueChange = onTextFieldValueChange,
                        leadingIcon = {
                            IconButton(onClick = onAddImageClick) {
                                Icon(
                                    imageVector = Icons.Filled.AttachFile,
                                    contentDescription = "attach image",
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = onSendMessageButtonClick) {
                                Icon(
                                    imageVector = Icons.Filled.Send,
                                    contentDescription = "send message",
                                    tint = MaterialTheme.colors.primaryVariant
                                )
                            }
                        },
                        placeholder = {Text(text = "Ваше сообщение...")}
                    )
                }
                if(!isDoctor) {
                    BottomNavBar(bottomNavBarNavigationEventSender, 2)
                }
            }

        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingGif(Modifier.size(60.dp))
                }
            }
            else {
                LazyColumn(state = lazyListState) {
                    itemsIndexed(messageList) { index, message ->
                        Row(
                            horizontalArrangement = if (message.sender == userID) Arrangement.End else Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .widthIn(max = 270.dp),
                                elevation = 4.dp,
                                color = if (message.sender == userID) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant,
                                shape = RoundedCornerShape(5.dp),

                                ) {
                                Text(
                                    text = message.text,
                                    modifier = Modifier.padding(
                                        vertical = 8.dp,
                                        horizontal = 10.dp
                                    ),
                                    color = if (isSystemInDarkTheme()) White500 else White200
                                )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    HIVmanagerTheme {
        ChatScreenUi()
    }
}
@Preview
@Composable
private fun ChatNotAvailablePreview() {
    HIVmanagerTheme {
        ChatNowAvailableUi()
    }
}