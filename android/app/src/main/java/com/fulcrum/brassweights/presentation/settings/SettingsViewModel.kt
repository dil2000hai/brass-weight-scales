package com.fulcrum.brassweights.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fulcrum.brassweights.domain.model.GradeLevel
import com.fulcrum.brassweights.domain.repository.ProgressRepository
import com.fulcrum.brassweights.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val resetVisible = MutableStateFlow(false)

    val state: StateFlow<SettingsUiState> = combine(
        settingsRepository.settings,
        progressRepository.progress,
        resetVisible
    ) { settings, progress, visible ->
        SettingsUiState(
            settings = settings,
            resetVisible = visible,
            solvedCount = progress.solvedCount
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(
            settings = settingsRepository.settings.value,
            resetVisible = false,
            solvedCount = progressRepository.progress.value.solvedCount
        )
    )

    fun onSoundChanged(enabled: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(sound = enabled))
    }

    fun onHapticsChanged(enabled: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(haptics = enabled))
    }

    fun onSymbolsChanged(enabled: Boolean) {
        settingsRepository.update(settingsRepository.settings.value.copy(showSymbols = enabled))
    }

    fun onGradeChanged(grade: GradeLevel) {
        settingsRepository.update(settingsRepository.settings.value.copy(gradeLevel = grade))
    }

    fun onResetRequested() {
        resetVisible.value = true
    }

    fun onResetDismissed() {
        resetVisible.value = false
    }

    fun onResetConfirmed() {
        progressRepository.reset()
        resetVisible.value = false
    }
}
