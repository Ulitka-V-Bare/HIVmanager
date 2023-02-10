package com.example.hivmanager.ui.screens.pillsreminder

sealed class MyPillsEvent {
    data class OnDeletePillInfoClick(val index:Int):MyPillsEvent()
    object OnAddNewPillInfoClick:MyPillsEvent()
}