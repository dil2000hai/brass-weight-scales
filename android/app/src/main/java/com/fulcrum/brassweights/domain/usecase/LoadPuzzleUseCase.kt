package com.fulcrum.brassweights.domain.usecase

import com.fulcrum.brassweights.domain.model.Puzzle

class LoadPuzzleUseCase(private val puzzles: List<Puzzle>) {

    val total: Int get() = puzzles.size

    operator fun invoke(index: Int): Puzzle = puzzles[index.coerceIn(0, puzzles.lastIndex)]
}
