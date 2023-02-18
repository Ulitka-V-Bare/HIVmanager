package com.example.hivmanager.ui.screens.addpill

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val notificationHelper: NotificationHelper,
    @ApplicationContext
    val context: Context
) : ViewModel() {

    /** состояние, при изменении State происходит отрисовка экрана
     * */
    var state by mutableStateOf(AddPillState())
        private set

    /** неоходимо для отправки событий навигации в composable функцию
     * */
    private val _navigationEvent = Channel<NavigationEvent>()
    val uiEvent = _navigationEvent.receiveAsFlow()


    /** обработка событий интерфейса
     * */
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
    /** если поле с количеством дней пустое, то выводится сообщение об этом
     * если не пустое, то создается экземпляр pillInfo и назначаются напоминания,
     * после чего происходит возврат на экран со списком напоминаний
     * */
    private fun onConfirmClick() {
        viewModelScope.launch {
            if(state.pillDuration.isEmpty()){
                Toast.makeText(context,"Укажите количество дней", Toast.LENGTH_SHORT).show()
                return@launch
            }
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
            _navigationEvent.send(NavigationEvent.NavigateUp)
        }
    }


    /** удаление времени напоминания из списка
     * */
    private fun onDeletePillTimeClick(index: Int) {
        state = state.copy(
            pillTime = state.pillTime.toMutableList().let {
                it.removeAt(index)
                it
            }
        )
//        state.pillTime.removeAt(index)
    }
    /** добавление времени напоминания в список
     * */
    private fun onPillTimeAdded(pillTime: String) {
        state = state.copy(
            pillTime = state.pillTime.plus(pillTime)
        )
//        state.pillTime.add(pillTime)
    }
    /** изменение времени начала приема
     * */
    private fun onPillStartChange(date: LocalDate) {
        state = state.copy(
            pillStart = date
        )
    }

    /** изменение названия препарата
     * */
    private fun onPillNameChange(pillName: String) {
        state = state.copy(
            pillName = pillName
        )
    }

    /** регулярное выражение означает строки, где есть только цифры или пустые строки
     * */
    val durationRegex = Regex("^\\d*\$")

    /** изменение длительности приема препарата
     * */
    private fun onPillDurationChange(duration: String) {
        if (duration.matches(durationRegex) && duration!="0")
            state = state.copy(
                pillDuration = duration
            )
    }
    /** отправка навигационного события
     * */
    fun sendNavigationEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}