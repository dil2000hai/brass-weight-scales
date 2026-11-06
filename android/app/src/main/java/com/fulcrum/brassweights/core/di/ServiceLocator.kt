package com.fulcrum.brassweights.core.di

import android.content.Context
import com.fulcrum.brassweights.data.local.PreferencesStorage
import com.fulcrum.brassweights.data.repository.ProgressRepositoryImpl
import com.fulcrum.brassweights.data.repository.SettingsRepositoryImpl
import com.fulcrum.brassweights.data.sample.SampleData

object ServiceLocator {

    private var factory: AppViewModelFactory? = null

    val viewModelFactory: AppViewModelFactory
        get() = requireNotNull(factory)

    fun init(context: Context) {
        if (factory != null) return
        val storage = PreferencesStorage(context)
        factory = AppViewModelFactory(
            progressRepository = ProgressRepositoryImpl(storage),
            settingsRepository = SettingsRepositoryImpl(storage),
            sandboxWeights = SampleData.sandboxWeights,
            sandboxTray = SampleData.sandboxTray,
            puzzles = SampleData.puzzles
        )
    }
}
