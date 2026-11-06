package com.fulcrum.brassweights.domain.model

data class Progress(
    val solvedCount: Int = 0,
    val currentPuzzle: Int = 0,
    val bestStreak: Int = 0,
    val currentStreak: Int = 0,
    val hintsUsed: Int = 0,
    val masteryBySkill: Map<SkillTag, Int> = emptyMap()
)
