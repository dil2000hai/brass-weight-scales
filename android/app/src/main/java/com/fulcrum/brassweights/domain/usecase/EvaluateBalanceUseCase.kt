package com.fulcrum.brassweights.domain.usecase

import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.BalanceState
import com.fulcrum.brassweights.domain.model.Puzzle
import com.fulcrum.brassweights.domain.model.ShapeType

class EvaluateBalanceUseCase {

    operator fun invoke(puzzle: Puzzle, right: List<ShapeType>): BalanceState =
        tilt(puzzle.leftPan, right, puzzle.weights)

    fun tilt(
        left: List<ShapeType>,
        right: List<ShapeType>,
        weights: Map<ShapeType, Int>
    ): BalanceState {
        val leftSum = left.sumOf { weights[it] ?: 0 }
        val rightSum = right.sumOf { weights[it] ?: 0 }
        val span = maxOf(leftSum, rightSum, 1).toFloat()
        val ratio = ((rightSum - leftSum).toFloat() / span).coerceIn(-1f, 1f)
        val balanced = leftSum == rightSum && (left.isNotEmpty() || right.isNotEmpty())
        return BalanceState(
            leftSum = leftSum,
            rightSum = rightSum,
            angleDegrees = ratio * GameConfig.MAX_TILT_DEGREES,
            solved = balanced
        )
    }
}
