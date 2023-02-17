package com.example.hivmanager.ui.screens.addpill

import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate

data class AddPillState(
    val pillName:String = "",
    val pillStart:LocalDate = LocalDate.now(),
    val pillTime:List<String> = listOf(),
    val pillDuration:String = "1"
)