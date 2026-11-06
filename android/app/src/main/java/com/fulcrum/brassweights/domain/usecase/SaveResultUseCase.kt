package com.fulcrum.brassweights.domain.usecase

import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.Deduction
import com.fulcrum.brassweights.domain.model.Puzzle
import com.fulcrum.brassweights.domain.model.RoundResult
import com.fulcrum.brassweights.domain.repository.ProgressRepository

class SaveResultUseCase(private val progressRepository: ProgressRepository) {

    operator fun invoke(
        puzzle: Puzzle,
        solved: Boolean,
        moves: Int,
        hints: Int,
        deductions: List<Deduction>
    ) {
        val current = progressRepository.progress.value
        val stars = when {
            !solved -> 0
            hints == 0 && moves <= puzzle.optimalMoves + 1 -> 3
            hints <= 1 -> 2
            else -> 1
        }
        val streak = if (solved) current.currentStreak + 1 else 0
        val mastery = current.masteryBySkill.toMutableMap()
        if (solved) {
            mastery[puzzle.skill] = (mastery[puzzle.skill] ?: 0) + 1
        }
        val nextIndex = if (solved) {
            (puzzle.index + 1).coerceAtMost(GameConfig.TOTAL_PUZZLES - 1)
        } else {
            puzzle.index
        }
        progressRepository.persist(
            current.copy(
                solvedCount = if (solved) current.solvedCount + 1 else current.solvedCount,
                currentPuzzle = nextIndex,
                bestStreak = maxOf(current.bestStreak, streak),
                currentStreak = streak,
                hintsUsed = current.hintsUsed + hints,
                masteryBySkill = mastery
            )
        )
        progressRepository.publishResult(
            RoundResult(
                solved = solved,
                puzzleNumber = puzzle.index + 1,
                chapter = puzzle.chapter,
                moves = moves,
                hints = hints,
                stars = stars,
                streak = streak,
                deductions = deductions,
                answerCount = puzzle.optimalMoves,
                answerShape = puzzle.targetShape
            )
        )
    }
}
