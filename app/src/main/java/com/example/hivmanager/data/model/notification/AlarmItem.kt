package com.example.hivmanager.data.model.notification

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)