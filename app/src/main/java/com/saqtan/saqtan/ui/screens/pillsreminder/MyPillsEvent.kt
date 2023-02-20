package com.saqtan.saqtan.ui.screens.pillsreminder

sealed class MyPillsEvent {
    data class OnDeletePillInfoClick(val index:Int):MyPillsEvent()
    object OnAddNewPillInfoClick:MyPillsEvent()
}