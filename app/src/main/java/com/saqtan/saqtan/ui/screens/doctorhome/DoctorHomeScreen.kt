package com.saqtan.saqtan.ui.screens.doctorhome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.ui.screens.components.MyTopAppBar
import com.saqtan.saqtan.ui.screens.components.SignOutDialog
import com.saqtan.saqtan.ui.theme.HIVmanagerTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun DoctorHomeScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: DoctorHomeViewModel = hiltViewModel()
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
    DoctorHomeScreenUi(
        patientList = viewModel.userRepository.patientList,
        messages = viewModel.state.messages,
        onOpenChatClick = {viewModel.navigateToChat(it)},
        onUserInfoClick = {viewModel.navigateToInfo(it)},
        onSignOutClick = {viewModel.onSignOutClick()}
    )
}

@Composable
fun DoctorHomeScreenUi(
    patientList: List<String> = listOf("dfsakasdf;j", "sdfwuqeirh"),
    messages: Map<String, String> = mapOf(),
    onOpenChatClick: (String) -> Unit = {},
    onUserInfoClick: (String)->Unit = {},
    onSignOutClick: ()->Unit = {}
) {
    val dialogState = rememberMaterialDialogState()
    SignOutDialog(dialogState = dialogState,
    onConfirmClick = onSignOutClick)
    Scaffold(
        topBar = { MyTopAppBar("Пациенты") },
        floatingActionButton = { FloatingActionButton(
            onClick = {dialogState.show()},
            backgroundColor = MaterialTheme.colors.primaryVariant,
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 15))
        ) {
            Text(text = "Выход",modifier = Modifier.padding(horizontal = 24.dp),color =  Color.White )
        }},
        floatingActionButtonPosition = FabPosition.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            itemsIndexed(patientList) { index, item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clickable { onOpenChatClick(item) },
                    elevation = 4.dp,
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary, fontSize = 20.sp)) {
                                    append("id: ")
                                }
                                append(item)
                            }, fontSize = 16.sp)
                            IconButton(onClick = {onUserInfoClick(item)}, modifier = Modifier.size(24.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "go to user info",
                                    tint = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            //   text = if(messages.containsKey(item)) "message: ${messages[item]!!}" else "...",
                            text = buildAnnotatedString {
                                if (messages.containsKey(item)) {
                                    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                                        append("message: ")
                                    }
                                    append(messages[item]!!)
                                } else {
                                    append("...")
                                }
                            },
                            maxLines = 2
                        )
                    }
                }
            }
            item{
                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }

}

@Preview
@Composable
fun DoctorHomeScreenPreview() {
    HIVmanagerTheme {
        DoctorHomeScreenUi()
    }
}