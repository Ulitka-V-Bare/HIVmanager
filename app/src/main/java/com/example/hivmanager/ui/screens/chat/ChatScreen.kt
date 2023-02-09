package com.example.hivmanager.ui.screens.chat

import androidx.compose.foundation.layout.*
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
    viewModel: InfoViewModel = hiltViewModel()
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
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)}
    )
}


@Composable
private fun ChatScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {},
    textFieldValue:String = "",
    onTextFieldValueChange:(String)->Unit = {},
    onSendMessageButtonClick: ()->Unit = {}
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
            Column(modifier = Modifier.constrainAs(messages){
                top.linkTo(parent.top)
                bottom.linkTo(input.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {

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