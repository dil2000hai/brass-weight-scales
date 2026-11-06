package com.fulcrum.brassweights.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Game : Screen
    data class GameOver(val solved: Boolean, val moves: Int, val hints: Int) : Screen
    data object FreeScale : Screen
    data object Settings : Screen
}
