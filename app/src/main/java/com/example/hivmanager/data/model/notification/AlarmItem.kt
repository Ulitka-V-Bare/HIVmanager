package com.example.hivmanager.data.model.notification

import java.time.LocalDateTime

/**
 * вспомогательный класс для создания уведомлений*/
data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)