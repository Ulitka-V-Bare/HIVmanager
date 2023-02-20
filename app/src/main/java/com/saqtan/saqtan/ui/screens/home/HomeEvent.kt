package com.saqtan.saqtan.ui.screens.home

sealed class HomeEvent {
    data class OnConfirmEditAllergiesClick(val allergies:String):HomeEvent()
    data class OnConfirmEditHeightClick(val height:String):HomeEvent()
    object OnSignOutClick:HomeEvent()
}