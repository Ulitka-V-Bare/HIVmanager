package com.example.hivmanager.navigation

sealed class NavigationEvent {
    data class Navigate(val route: String, val popBackStack: Boolean = false): NavigationEvent()
    object NavigateUp:NavigationEvent()
}