package com.example.hivmanager.ui.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.screens.info.InfoViewModel
import com.example.hivmanager.ui.theme.HIVmanagerTheme

@Composable
fun ChatScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route, it.popBackStack)
                }
                else -> {}
            }
        }
    }
    ChatScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)},
        textFieldValue = viewModel.state.message,
        onTextFieldValueChange = {viewModel.onEvent(ChatEvent.OnMessageValueChange(it))},
        onSendMessageButtonClick = {viewModel.onEvent(ChatEvent.OnSendMessageButtonClick)},
        messageList = viewModel.state.allMessages
    )
}


@Composable
private fun ChatScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {},
    textFieldValue:String = "",
    onTextFieldValueChange:(String)->Unit = {},
    onSendMessageButtonClick: ()->Unit = {},
    messageList:List<Message> = listOf()
){
    Scaffold(
        topBar = { MyTopAppBar("Chat") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender,2) }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val(messages,input) = createRefs()
            LazyColumn(modifier = Modifier.constrainAs(messages){
                top.linkTo(parent.top)
                bottom.linkTo(input.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                itemsIndexed(messageList){index,message->
                    Text(text = message.text)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(input) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = textFieldValue,
                    onValueChange = onTextFieldValueChange,
                    trailingIcon = {
                        IconButton(onClick = onSendMessageButtonClick) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "send message",
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview(){
    HIVmanagerTheme {
        ChatScreenUi()
    }
}