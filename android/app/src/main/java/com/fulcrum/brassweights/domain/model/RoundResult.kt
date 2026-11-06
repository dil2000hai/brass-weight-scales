package com.fulcrum.brassweights.domain.model

data class RoundResult(
    val solved: Boolean,
    val puzzleNumber: Int,
    val chapter: Chapter,
    val moves: Int,
    val hints: Int,
    val stars: Int,
    val streak: Int,
    val deductions: List<Deduction>,
    val answerCount: Int,
    val answerShape: ShapeType
)
