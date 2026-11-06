package com.fulcrum.brassweights.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fulcrum.brassweights.domain.model.Puzzle
import com.fulcrum.brassweights.domain.model.ShapeType
import com.fulcrum.brassweights.domain.repository.ProgressRepository
import com.fulcrum.brassweights.domain.repository.SettingsRepository
import com.fulcrum.brassweights.domain.usecase.BuildHintUseCase
import com.fulcrum.brassweights.domain.usecase.EvaluateBalanceUseCase
import com.fulcrum.brassweights.domain.usecase.LoadPuzzleUseCase
import com.fulcrum.brassweights.domain.usecase.SaveResultUseCase
import com.fulcrum.brassweights.presentation.app.AppViewModel
import com.fulcrum.brassweights.presentation.freescale.FreeScaleViewModel
import com.fulcrum.brassweights.presentation.game.GameViewModel
import com.fulcrum.brassweights.presentation.gameover.GameOverViewModel
import com.fulcrum.brassweights.presentation.menu.MenuViewModel
import com.fulcrum.brassweights.presentation.settings.SettingsViewModel
import com.fulcrum.brassweights.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val sandboxWeights: Map<ShapeType, Int>,
    private val sandboxTray: List<ShapeType>,
    puzzles: List<Puzzle>
) : ViewModelProvider.Factory {

    private val loadPuzzle = LoadPuzzleUseCase(puzzles)
    private val evaluateBalance = EvaluateBalanceUseCase()
    private val buildHint = BuildHintUseCase()
    private val saveResult = SaveResultUseCase(progressRepository)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(progressRepository, loadPuzzle)
            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(progressRepository, loadPuzzle, evaluateBalance, buildHint, saveResult)
            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(progressRepository)
            modelClass.isAssignableFrom(FreeScaleViewModel::class.java) ->
                FreeScaleViewModel(evaluateBalance, sandboxWeights, sandboxTray)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settingsRepository, progressRepository)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
