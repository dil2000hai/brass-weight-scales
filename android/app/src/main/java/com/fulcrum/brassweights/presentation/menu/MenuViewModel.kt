package com.fulcrum.brassweights.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.Progress
import com.fulcrum.brassweights.domain.repository.ProgressRepository
import com.fulcrum.brassweights.domain.usecase.LoadPuzzleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val loadPuzzle: LoadPuzzleUseCase
) : ViewModel() {

    private val skillsVisible = MutableStateFlow(false)

    val state: StateFlow<MenuUiState> =
        combine(progressRepository.progress, skillsVisible) { progress, visible ->
            toUiState(progress, visible)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = toUiState(progressRepository.progress.value, false)
        )

    fun onSkillsOpen() {
        skillsVisible.value = true
    }

    fun onSkillsDismiss() {
        skillsVisible.value = false
    }

    private fun toUiState(progress: Progress, visible: Boolean): MenuUiState {
        val puzzle = loadPuzzle(progress.currentPuzzle)
        return MenuUiState(
            solvedCount = progress.solvedCount,
            total = loadPuzzle.total,
            bestStreak = progress.bestStreak,
            masteryPercent = progress.solvedCount * 100 / GameConfig.TOTAL_PUZZLES,
            hintsUsed = progress.hintsUsed,
            chapter = puzzle.chapter,
            puzzleInChapter = progress.currentPuzzle % GameConfig.PUZZLES_PER_CHAPTER + 1,
            skillMastery = progress.masteryBySkill,
            skillsVisible = visible
        )
    }
}
