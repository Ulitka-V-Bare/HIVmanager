package com.saqtan.saqtan.ui.screens.doctorhome

data class DoctorHomeState(
    val messages:Map<String,String> = mapOf(),
    val patientID:String? = null
)