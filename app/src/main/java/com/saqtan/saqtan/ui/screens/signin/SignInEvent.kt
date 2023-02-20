package com.saqtan.saqtan.ui.screens.signin

import android.app.Activity

sealed class SignInEvent {
    data class OnSendCodeButtonClick(val activity: Activity):SignInEvent()
    object OnSignInButtonClick:SignInEvent()
    object OnResendCodeButtonClick:SignInEvent()
    data class OnCodeChange(val code:String):SignInEvent()
    data class OnPhoneNumberChange(val phoneNumber:String):SignInEvent()
    object OnChangePhoneNumberClick:SignInEvent()
}