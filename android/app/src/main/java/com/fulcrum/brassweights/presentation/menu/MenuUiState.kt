package com.fulcrum.brassweights.presentation.menu

import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.Chapter
import com.fulcrum.brassweights.domain.model.SkillTag

data class MenuUiState(
    val solvedCount: Int = 0,
    val total: Int = GameConfig.TOTAL_PUZZLES,
    val bestStreak: Int = 0,
    val masteryPercent: Int = 0,
    val hintsUsed: Int = 0,
    val chapter: Chapter = Chapter.FIRST_BALANCE,
    val puzzleInChapter: Int = 1,
    val skillMastery: Map<SkillTag, Int> = emptyMap(),
    val skillsVisible: Boolean = false
)
