package com.example.hivmanager.ui.screens.addpill

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hivmanager.data.model.PillInfo
import com.example.hivmanager.data.model.notification.AlarmItem
import com.example.hivmanager.data.model.notification.AlarmScheduler
import com.example.hivmanager.data.model.notification.NotificationHelper
import com.example.hivmanager.data.repository.UserRepository
import com.example.hivmanager.navigation.NavigationEvent
import com.example.hivmanager.navigation.Route
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddPillViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userRepository: UserRepository,
    val scheduler: AlarmScheduler,
    val notificationHelper: NotificationHelper
) : ViewModel() {


    var state by mutableStateOf(AddPillState())
        private set

    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()


    fun onEvent(event: AddPillEvent) {
        when (event) {
            is AddPillEvent.OnPillNameChange -> onPillNameChange(event.pillName)
            is AddPillEvent.OnPillDurationChange -> onPillDurationChange(event.pillDuration)
            is AddPillEvent.OnPillStartChange -> onPillStartChange(event.date)
            is AddPillEvent.OnPillTimeAdded -> onPillTimeAdded(event.pillTime)
            is AddPillEvent.OnDeletePillTimeClick -> onDeletePillTimeClick(event.pillTimeIndex)
            AddPillEvent.OnConfirmClick -> onConfirmClick()
        }
    }

    private fun onConfirmClick() {
        viewModelScope.launch {
            val pillInfo = PillInfo(
                name = state.pillName,
                startDate = DateTimeFormatter.ofPattern("dd.MM.YYYY").format(state.pillStart),
                finishDate = DateTimeFormatter.ofPattern("dd.MM.YYYY")
                    .format(state.pillStart.plusDays(state.pillDuration.toLong() - 1)),
                timeToTakePill = state.pillTime,
                duration = state.pillDuration.toInt()
            )
            userRepository.addPillInfo(
                pillInfo
            )
            notificationHelper.createNotificationRequest(pillInfo, scheduler)
            _navigationEvent.send(NavigationEvent.Navigate(route = Route.pillReminder))
        }
    }



    private fun onDeletePillTimeClick(index: Int) {
        state = state.copy(
            pillTime = state.pillTime.toMutableList().let {
                it.removeAt(index)
                it
            }
        )
//        state.pillTime.removeAt(index)
    }

    private fun onPillTimeAdded(pillTime: String) {
        state = state.copy(
            pillTime = state.pillTime.plus(pillTime)
        )
//        state.pillTime.add(pillTime)
    }

    private fun onPillStartChange(date: LocalDate) {
        state = state.copy(
            pillStart = date
        )
    }

    private fun onPillNameChange(pillName: String) {
        state = state.copy(
            pillName = pillName
        )
    }

    val durationRegex = Regex("^\\d*\$")
    private fun onPillDurationChange(duration: String) {
        if (duration.matches(durationRegex))
            state = state.copy(
                pillDuration = duration
            )
    }

    fun sendNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}