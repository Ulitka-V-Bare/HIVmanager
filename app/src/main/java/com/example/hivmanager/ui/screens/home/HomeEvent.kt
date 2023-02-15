package com.example.hivmanager.ui.screens.home

sealed class HomeEvent {
    object OnEditHeightClick:HomeEvent()
    data class OnHeightChange(val height:Int):HomeEvent()
    object OnEditAllergiesClick:HomeEvent()
    data class OnAllergiesChange(val allergies:String):HomeEvent()

    object OnCancelEditAllergiesClick:HomeEvent()
    object OnCancelEditHeightClick:HomeEvent()
    data class OnConfirmEditAllergiesClick(val allergies:String):HomeEvent()
    data class OnConfirmEditHeightClick(val height:String):HomeEvent()
}