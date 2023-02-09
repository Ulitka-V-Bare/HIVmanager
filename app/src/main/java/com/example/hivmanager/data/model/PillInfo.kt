package com.example.hivmanager.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



data class PillInfo(
    val name: String = "",
    val startDate:LocalDate = LocalDate.now(),
    val finishDate:LocalDate = LocalDate.now().plusWeeks(1),
    val timeToTakePill: MutableList<String> = mutableListOf()
)

val PillInfo_example = PillInfo(
    name = "Аспирин",
    timeToTakePill = mutableListOf("14:00","18:00","19:00","20:00","21:00")
)