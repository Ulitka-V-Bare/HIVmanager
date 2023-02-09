package com.example.hivmanager.ui.screens.addpill

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.ui.screens.components.BottomNavBar
import com.example.hivmanager.ui.screens.components.MyTopAppBar
import com.example.hivmanager.ui.theme.HIVmanagerTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import org.checkerframework.checker.units.qual.s
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed

@Composable
fun AddPillScreen(
    onNavigate: (route: String, popBackStack: Boolean) -> Unit,
    viewModel: AddPillViewModel = hiltViewModel()
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
    AddPillScreenUi(
        bottomNavBarNavigationEventSender = {viewModel.sendNavigationEvent(it)},
        pillName = viewModel.state.pillName,
        onPillNameChange = {viewModel.onEvent(AddPillEvent.OnPillNameChange(it))},
        pillStart = DateTimeFormatter.ofPattern("dd.MM.YYYY").format(viewModel.state.pillStart),
        onPillStartDatePick = {viewModel.onEvent(AddPillEvent.OnPillStartChange(it))},
        pillDuration = viewModel.state.pillDuration,
        onPillDurationChange = {viewModel.onEvent(AddPillEvent.OnPillDurationChange(it))},
        pillTime = viewModel.state.pillTime,
        onTimeAdded = {viewModel.onEvent(AddPillEvent.OnPillTimeAdded(it))},
        onDeletePillTimeClick = {viewModel.onEvent(AddPillEvent.OnDeletePillTimeClick(it))}
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AddPillScreenUi(
    bottomNavBarNavigationEventSender:(NavigationEvent)->Unit = {},
    pillName:String = "Аспирин",
    onPillNameChange:(String)->Unit = {},
    pillStart:String = "Аспирин",
    onPillStartDatePick:(LocalDate)->Unit = {},
    pillTime:List<String> = listOf("19:00"),
  //  onPillTimeChange:(String)->Unit = {},
    pillDuration:String = "Аспирин",
    onPillDurationChange:(String)->Unit = {},
    onTimeAdded: (String) -> Unit = {},
    onDeletePillTimeClick:(Int)->Unit = {}
){
    val dateDialogState = rememberMaterialDialogState()

    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mTimePickerDialog = TimePickerDialog(
        LocalContext.current,
        {_, mHour : Int, mMinute: Int ->
            if("$mMinute".length==1)
                onTimeAdded("$mHour:0$mMinute")
            else
                onTimeAdded("$mHour:$mMinute")
        }, mHour, mMinute, true
    )

    Scaffold(
        topBar = { MyTopAppBar("Info") },
        bottomBar = { BottomNavBar(bottomNavBarNavigationEventSender,0) }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .padding(horizontal = 24.dp)) {
            datePickerDialog(dateDialogState = dateDialogState, onDatePick = onPillStartDatePick)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Медикамент")
            AddPillTextField(
                value = pillName,
                onValueChange = onPillNameChange,
                label = {Text(text = "Название и разовая дозировка")}
                )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Начало")
            AddPillTextField(
                value = pillStart,
                onValueChange = {},
                label = {Text(text = "Первый день приема")},
                onClick = {
                    Log.d("AddPillScreen","entered onClick")
                    dateDialogState.show()
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Время приема")
            LazyColumn{
                itemsIndexed(pillTime){ index,time ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        AddPillTextField(
                            value = time,
                            onValueChange = {},
                            label = {Text(text = "Время напоминания")},
                            onClick = {}
                        )
                        IconButton(
                            onClick = {onDeletePillTimeClick(index)}
                        ){
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "delete pill time",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }


            Button(onClick = { mTimePickerDialog.show() }) {
                Text(text ="Добавить время")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Длительность курса")
            AddPillTextField(
                value = pillDuration,
                onValueChange = onPillDurationChange,
                label = {Text(text = "Количество дней")}
            )
        }
    }
}


@Composable
private fun datePickerDialog(
    dateDialogState:MaterialDialogState,
    onDatePick:(LocalDate)->Unit = {}
){
    MaterialDialog (
        dialogState = dateDialogState,
        properties = DialogProperties (
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker (
            initialDate = LocalDate.now(),
            title = "Pick a date"
        ) {
            onDatePick(it)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AddPillTextField(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
    value: String = "",
    label: @Composable (() -> Unit)? = null,
) {
    if (onClick == null) {
        TextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            label=label
        )
    } else {

        TextField(
            modifier=modifier.clickable { onClick() },
            enabled = false,
            value = value,
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                disabledLabelColor =  MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),

            ),
            label=label
        )

    }
}

@Preview
@Composable
private fun AddPillScreenPreview(){
    HIVmanagerTheme {
        AddPillScreenUi()
    }
}