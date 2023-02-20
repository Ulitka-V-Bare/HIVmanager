package com.saqtan.saqtan.ui.screens.addpill

import java.time.LocalDate

/** текущие значения переменный в экране viewModel
 * */
data class AddPillState(
    val pillName:String = "",
    val pillStart:LocalDate = LocalDate.now(),
    val pillTime:List<String> = listOf(),
    val pillDuration:String = "1"
)