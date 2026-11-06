package com.fulcrum.brassweights.domain.repository

import com.fulcrum.brassweights.domain.model.Progress
import com.fulcrum.brassweights.domain.model.RoundResult
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<Progress>
    val lastResult: StateFlow<RoundResult?>
    fun persist(progress: Progress)
    fun publishResult(result: RoundResult)
    fun reset()
}
