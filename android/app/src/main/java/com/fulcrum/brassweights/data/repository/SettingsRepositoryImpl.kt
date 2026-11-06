package com.fulcrum.brassweights.data.repository

import com.fulcrum.brassweights.data.local.PreferencesStorage
import com.fulcrum.brassweights.domain.model.AppSettings
import com.fulcrum.brassweights.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val storage: PreferencesStorage) : SettingsRepository {

    private val _settings = MutableStateFlow(storage.readSettings())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun update(settings: AppSettings) {
        storage.writeSettings(settings)
        _settings.value = settings
    }
}
