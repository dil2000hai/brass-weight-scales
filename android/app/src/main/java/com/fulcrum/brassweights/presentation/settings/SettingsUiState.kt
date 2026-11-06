package com.fulcrum.brassweights.presentation.settings

import com.fulcrum.brassweights.domain.model.AppSettings

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val resetVisible: Boolean = false,
    val solvedCount: Int = 0
)
