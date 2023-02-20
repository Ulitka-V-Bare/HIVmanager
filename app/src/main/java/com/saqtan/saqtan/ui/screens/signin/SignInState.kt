package com.saqtan.saqtan.ui.screens.signin

data class SignInState (
    val phoneNumber: String = "",
    val code: String = "",
    val timerSeconds:Int = 0,
    val isCodeError:Boolean = false
)