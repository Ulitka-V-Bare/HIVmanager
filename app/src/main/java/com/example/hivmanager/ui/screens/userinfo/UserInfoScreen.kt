package com.example.hivmanager.ui.screens.userinfo

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.ui.screens.addpill.AddPillViewModel
import com.example.hivmanager.ui.screens.diary.DiaryEvent
import com.example.hivmanager.ui.screens.diary.DiaryViewModel
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.data.model.DiaryEntry
import com.example.hivmanager.data.model.UserData
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.screens.components.OutlinedTextFieldNoContentPadding
import com.example.hivmanager.ui.screens.doctorhome.DoctorHomeViewModel
import com.example.hivmanager.ui.theme.HIVmanagerTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.asFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun UserInfoScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: UserInfoViewModel = hiltViewModel()
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
    DiaryScreenUi(
        diaryList = viewModel.userData.diaryEntries,
        onBackClick = {viewModel.sendNavigationEvent(NavigationEvent.NavigateUp)}
    )
}




@Composable
private fun DiaryScreenUi(
    diaryList: List<DiaryEntry> = listOf(diaryEntryExample),
    onBackClick:()->Unit = {}
) {
    Scaffold(
        topBar = { MyTopAppBar("Дневник", onBackClick = onBackClick) },
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                DiaryHeader()
            }
            items(diaryList) { diaryEntry ->
                DiaryEntryContainer(diaryEntry)
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DiaryHeader() {
    Row(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "СИС", color = MaterialTheme.colors.primary, fontSize = 20.sp)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "ДИА", color = MaterialTheme.colors.primary, fontSize = 20.sp)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Пульс", color = MaterialTheme.colors.primary, fontSize = 20.sp)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Темп", color = MaterialTheme.colors.primary, fontSize = 20.sp)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Вес", color = MaterialTheme.colors.primary, fontSize = 20.sp)
        }
    }
}

@Composable
private fun DiaryEntryContainer(
    diaryEntry: DiaryEntry = diaryEntryExample,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(8.dp),
        elevation = 6.dp
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${diaryEntry.time}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${diaryEntry.upperTension}",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${diaryEntry.lowerTension}",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${diaryEntry.pulse}",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${diaryEntry.temperature}",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${diaryEntry.weight}",
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp
                    )
                }
            }
            Row(Modifier.fillMaxWidth()) {
                commentHolder(comment = diaryEntry.comment)
            }
        }
    }
}

@Composable
private fun commentHolder(comment: String = "my comment") {
    var isExpanded by remember { mutableStateOf(false) }
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Комментарий", modifier = Modifier.padding(start = 8.dp))
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "expend more/less article text",
                    tint = if (isExpanded) MaterialTheme.colors.primaryVariant else LocalContentColor.current.copy(
                        alpha = LocalContentAlpha.current
                    )
                )
            }
        }
        AnimatedVisibility(visible = isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(text = comment, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
            }
        }
    }
}

@Preview
@Composable
private fun DiaryScreenPreview() {
    HIVmanagerTheme {
        DiaryScreenUi()
    }
}

private val diaryEntryExample = DiaryEntry(
    120,
    80,
    60.5,
    37.2,
    83,
    "комментарий",
    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
)