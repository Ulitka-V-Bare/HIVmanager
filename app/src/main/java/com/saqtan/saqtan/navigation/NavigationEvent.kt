package com.saqtan.saqtan.navigation

/** вспомогательный класс для хранения видов событий навигации, если нужно использовать еще какие-то
 * функции навигации помимо приведенный, нужно добавить соответствующее событие и обработать его в
 * LaunchedEffects в composable функциях
 * */
sealed class NavigationEvent {
    data class Navigate(val route: String, val popBackStack: Boolean = false): NavigationEvent()
    object NavigateUp:NavigationEvent()
}