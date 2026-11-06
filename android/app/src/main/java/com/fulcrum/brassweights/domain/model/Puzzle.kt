package com.fulcrum.brassweights.domain.model

data class Puzzle(
    val index: Int,
    val chapter: Chapter,
    val skill: SkillTag,
    val weights: Map<ShapeType, Int>,
    val clues: List<ClueScale>,
    val leftPan: List<ShapeType>,
    val trayShapes: List<ShapeType>,
    val targetShape: ShapeType,
    val optimalMoves: Int
)
