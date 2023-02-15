package com.example.hivmanager.ui.screens.home

data class HomeState(
    val isEditingHeight:Boolean = false,
    val isEditingAllergies:Boolean = false,
    val height:Int = 0,
    val allergies: String = "",
)