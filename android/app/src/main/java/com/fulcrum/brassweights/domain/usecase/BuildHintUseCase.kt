package com.fulcrum.brassweights.domain.usecase

import com.fulcrum.brassweights.domain.model.Deduction
import com.fulcrum.brassweights.domain.model.Puzzle

class BuildHintUseCase {

    operator fun invoke(puzzle: Puzzle): List<Deduction> = puzzle.clues.mapNotNull { clue ->
        val left = clue.left.firstOrNull()
        val right = clue.right.firstOrNull()
        if (left == null || right == null) {
            null
        } else {
            Deduction(clue.left.size, left, clue.right.size, right)
        }
    }
}
