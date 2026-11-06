package com.fulcrum.brassweights.presentation.game

import com.fulcrum.brassweights.domain.model.Deduction
import com.fulcrum.brassweights.domain.model.Puzzle
import com.fulcrum.brassweights.domain.model.ShapeType

enum class GamePhase {
    IDLE,
    PLACING,
    CHECKING,
    UNBALANCED,
    SOLVED,
    ABANDONED
}

enum class TiltSide {
    NONE,
    LEFT,
    RIGHT
}

data class GameUiState(
    val puzzle: Puzzle? = null,
    val rightPan: List<ShapeType> = emptyList(),
    val angle: Float = 0f,
    val phase: GamePhase = GamePhase.IDLE,
    val moves: Int = 0,
    val hints: Int = 0,
    val selectedShape: ShapeType? = null,
    val tilt: TiltSide = TiltSide.NONE,
    val deductions: List<Deduction> = emptyList(),
    val revealedHints: Int = 0,
    val hintVisible: Boolean = false,
    val pauseVisible: Boolean = false,
    val showEquation: Boolean = false,
    val puzzleInChapter: Int = 1,
    val chapterSize: Int = 20
) {
    val interactive: Boolean
        get() = phase == GamePhase.IDLE || phase == GamePhase.PLACING || phase == GamePhase.UNBALANCED
}
