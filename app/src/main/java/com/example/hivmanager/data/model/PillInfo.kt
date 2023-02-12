package com.example.hivmanager.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Serializable
data class PillInfo(
    val name: String = "",
    val startDate:String = "19.02.2002",
    val finishDate:String = "22.02.2002",
    val timeToTakePill: List<String> = listOf(),
    val duration:Int = 0
)

val PillInfo_example = PillInfo(
    name = "Аспирин",
    timeToTakePill = mutableListOf("14:00","18:00","19:00","20:00","21:00")
)