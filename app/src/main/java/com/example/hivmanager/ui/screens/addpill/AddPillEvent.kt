package com.example.hivmanager.ui.screens.addpill

import java.time.LocalDate

/** события на экране AddPillScreen
 * */
sealed class AddPillEvent {
    data class OnPillNameChange(val pillName:String):AddPillEvent()
    data class OnPillStartChange(val date:LocalDate):AddPillEvent()
    data class OnPillDurationChange(val pillDuration:String):AddPillEvent()
    data class OnPillTimeAdded(val pillTime:String):AddPillEvent()
    data class OnDeletePillTimeClick(val pillTimeIndex:Int):AddPillEvent()

    object OnConfirmClick:AddPillEvent()
}