package com.fulcrum.brassweights.data.repository

import com.fulcrum.brassweights.data.local.PreferencesStorage
import com.fulcrum.brassweights.domain.model.Progress
import com.fulcrum.brassweights.domain.model.RoundResult
import com.fulcrum.brassweights.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(private val storage: PreferencesStorage) : ProgressRepository {

    private val _progress = MutableStateFlow(storage.readProgress())
    override val progress: StateFlow<Progress> = _progress.asStateFlow()

    private val _lastResult = MutableStateFlow<RoundResult?>(null)
    override val lastResult: StateFlow<RoundResult?> = _lastResult.asStateFlow()

    override fun persist(progress: Progress) {
        storage.writeProgress(progress)
        _progress.value = progress
    }

    override fun publishResult(result: RoundResult) {
        _lastResult.value = result
    }

    override fun reset() {
        val empty = Progress()
        storage.writeProgress(empty)
        _progress.value = empty
        _lastResult.value = null
    }
}
