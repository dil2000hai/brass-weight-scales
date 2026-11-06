package com.fulcrum.brassweights.presentation.gameover

import com.fulcrum.brassweights.domain.model.RoundResult

data class GameOverUiState(
    val result: RoundResult? = null,
    val bestStreak: Int = 0,
    val solvedCount: Int = 0
)
