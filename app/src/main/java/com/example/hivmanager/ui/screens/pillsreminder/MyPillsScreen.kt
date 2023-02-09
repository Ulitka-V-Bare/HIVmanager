package com.example.hivmanager.ui.screens.pillsreminder

import android.provider.ContactsContract
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.data.model.PillInfo
import com.example.hivmanager.data.model.PillInfo_example
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyFloatingActionButton
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.theme.HIVmanagerTheme

@Composable
fun MyPillsScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: MyPillsViewModel = hiltViewModel()
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
    MyPillsScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)},
        pillList = viewModel.getPills()
    )
}


@Composable
private fun MyPillsScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {},
    pillList:List<PillInfo> = listOf(PillInfo_example,PillInfo_example,PillInfo_example)
){
    Scaffold(
        topBar = { MyTopAppBar("My pills") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender,0) },
        floatingActionButton = { MyFloatingActionButton()}
    ) {
        Column(modifier = Modifier.padding(it)) {
            pillList.forEachIndexed { index, pillInfo ->
                MyPillCard(pillInfo)
            }
        }

    }
}

@Composable
fun MyPillCard(pillInfo: PillInfo = PillInfo_example, modifier: Modifier = Modifier){
    Surface(
        modifier = modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Row() {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colors.primary
                ) {}
            }
            Column(modifier = Modifier.weight(5f)) {
                Text(text = "${pillInfo.name}", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = buildAnnotatedString {
                        append("Время приема: ")
                        for(time in pillInfo.timeToTakePill) append("$time ")
                    }
                )
                Text(text = "Начало: ${pillInfo.startDate}")
                Text(text = "Конец: ${pillInfo.finishDate}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Preview
@Composable
private fun MyPillsScreenPreview(){
    HIVmanagerTheme {
        MyPillsScreenUi()
    }
}

@Preview
@Composable
private fun MyPillsCardPreview(){
    HIVmanagerTheme {
        MyPillCard()
    }
}