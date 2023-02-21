package com.saqtan.saqtan.ui.screens.pillsreminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.saqtan.saqtan.data.model.PillInfo
import com.saqtan.saqtan.data.model.PillInfo_example
import com.saqtan.saqtan.data.model.UserData
import com.saqtan.saqtan.navigation.NavigationEvent
import com.saqtan.saqtan.ui.screens.components.BottomNavBar
import com.saqtan.saqtan.ui.screens.components.MyFloatingActionButton
import com.saqtan.saqtan.ui.screens.components.MyTopAppBar
import com.saqtan.saqtan.ui.theme.HIVmanagerTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun MyPillsScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: MyPillsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it) {
                is NavigationEvent.Navigate -> {
                    onNavigate(it.route, it.popBackStack)
                }
                is NavigationEvent.NavigateUp -> {
                    onNavigateUp()
                }
                else -> {}
            }
        }
    }
    val pillInfoList = viewModel.userRepository.userDataFlow.collectAsState(initial = UserData()).value
    MyPillsScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)},
        pillList = pillInfoList.pillInfoList,
        onDeletePillClick = {viewModel.onEvent(MyPillsEvent.OnDeletePillInfoClick(it))},
        onAddNewPillClick = {viewModel.onEvent(MyPillsEvent.OnAddNewPillInfoClick)},
        onBackClick = {viewModel.sendNavigationEvent(NavigationEvent.NavigateUp)}
    )
}


@Composable
private fun MyPillsScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {},
    pillList:List<PillInfo> = listOf(PillInfo_example,PillInfo_example,PillInfo_example),
    onDeletePillClick:(Int)->Unit = {},
    onAddNewPillClick:()->Unit={},
    onBackClick:()->Unit = {}
){

    Scaffold(
        topBar = { MyTopAppBar("Менің ескертулерім",onBackClick = onBackClick) },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender) },
        floatingActionButton = { MyFloatingActionButton(onClick = onAddNewPillClick)}
    ) {

        Column(modifier = Modifier.padding(it)) {
            pillList.forEachIndexed { index, pillInfo ->
                val deletePillDialogState = rememberMaterialDialogState()
                MyPillCard(pillInfo = pillInfo, onDeletePillClick = {deletePillDialogState.show()})
                DeletePillDialog(
                    dialogState = deletePillDialogState,
                    onConfirmClick = {onDeletePillClick(index)})
            }
        }

    }
}


@Composable
fun DeletePillDialog(
    dialogState: MaterialDialogState,
    onConfirmClick: ()->Unit
){
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties (
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        buttons = {
            positiveButton(text = "Cақтау", onClick = onConfirmClick)
            negativeButton(text = "Болдырмау")
        }
    ) {
        Text(
            text= "Бұл ескертуді жоюға сенімдісіз бе?",
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}
/***
 * отображает информацию о напоминании pillInfo
 */
@Composable
fun MyPillCard(
    pillInfo: PillInfo = PillInfo_example,
    modifier: Modifier = Modifier,
    onDeletePillClick: () -> Unit = {}
){
    Surface(
        modifier = modifier.padding(start = 8.dp,end=8.dp,top=8.dp),
        elevation = 4.dp
    ) {
        Row() {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colors.primary
                ) {}
            }
            Column(modifier = Modifier.weight(5f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                    ) {
                    Text(
                        text = "${pillInfo.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(onClick = onDeletePillClick) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "delete reminder",
                        )
                    }
                }
                Text(
                    text = buildAnnotatedString {
                        append("Қабылдау уақыты: ")
                        for(time in pillInfo.timeToTakePill) append("$time ")
                    }
                )
                Text(text = "Бастау кезі: ${pillInfo.startDate}")
                Text(text = "Соңы: ${pillInfo.finishDate}")
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