package com.fulcrum.brassweights.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fulcrum.brassweights.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val state: StateFlow<GameOverUiState> =
        combine(progressRepository.lastResult, progressRepository.progress) { result, progress ->
            GameOverUiState(
                result = result,
                bestStreak = progress.bestStreak,
                solvedCount = progress.solvedCount
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GameOverUiState(
                result = progressRepository.lastResult.value,
                bestStreak = progressRepository.progress.value.bestStreak,
                solvedCount = progressRepository.progress.value.solvedCount
            )
        )
}
