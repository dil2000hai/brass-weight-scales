package com.fulcrum.brassweights.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fulcrum.brassweights.core.di.ServiceLocator
import com.fulcrum.brassweights.core.navigation.Screen
import com.fulcrum.brassweights.presentation.freescale.FreeScaleScreen
import com.fulcrum.brassweights.presentation.game.GameScreen
import com.fulcrum.brassweights.presentation.gameover.GameOverScreen
import com.fulcrum.brassweights.presentation.menu.MenuScreen
import com.fulcrum.brassweights.presentation.settings.SettingsScreen
import com.fulcrum.brassweights.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { fadeIn(tween(420)) togetherWith fadeOut(tween(300)) },
        label = "rootHost"
    ) { target ->
        when (target) {
            Screen.Splash -> SplashScreen(
                onFinished = { appViewModel.replaceRoot(Screen.Menu) }
            )

            Screen.Menu -> MenuScreen(
                onPlay = { appViewModel.navigate(Screen.Game) },
                onFreeScale = { appViewModel.navigate(Screen.FreeScale) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )

            Screen.Game -> GameScreen(
                onFinished = { solved, moves, hints ->
                    appViewModel.replaceCurrent(Screen.GameOver(solved, moves, hints))
                },
                onMenu = { appViewModel.replaceRoot(Screen.Menu) }
            )

            is Screen.GameOver -> GameOverScreen(
                solved = target.solved,
                moves = target.moves,
                hints = target.hints,
                onPlayAgain = { appViewModel.replaceCurrent(Screen.Game) },
                onMenu = { appViewModel.replaceRoot(Screen.Menu) }
            )

            Screen.FreeScale -> FreeScaleScreen(
                onBack = { appViewModel.back() }
            )

            Screen.Settings -> SettingsScreen(
                onBack = { appViewModel.back() }
            )
        }
    }
}
