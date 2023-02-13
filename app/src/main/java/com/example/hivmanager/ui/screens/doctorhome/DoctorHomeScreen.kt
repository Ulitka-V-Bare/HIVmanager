package com.example.hivmanager.ui.screens.doctorhome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.screens.home.HomeViewModel
import com.example.hivmanager.ui.theme.HIVmanagerTheme

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
        onOpenChatClick = {viewModel.navigateToChat(it)}
    )
}

@Composable
fun DoctorHomeScreenUi(
    patientList: List<String> = listOf("dfsakasdf;j", "sdfwuqeirh"),
    messages: Map<String, String> = mapOf(),
    onOpenChatClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = { MyTopAppBar("Пациенты") },
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
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                                append("id: ")
                            }
                            append(item)
                        }, fontSize = 20.sp)
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