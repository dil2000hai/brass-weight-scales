package com.fulcrum.brassweights.domain.repository

import com.fulcrum.brassweights.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<AppSettings>
    fun update(settings: AppSettings)
}
