package com.fulcrum.brassweights.presentation.app

import androidx.lifecycle.ViewModel
import com.fulcrum.brassweights.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun navigate(target: Screen) {
        if (_screen.value != Screen.Splash) {
            backStack.add(_screen.value)
        }
        _screen.value = target
        syncBack()
    }

    fun replaceCurrent(target: Screen) {
        _screen.value = target
        syncBack()
    }

    fun replaceRoot(target: Screen) {
        backStack.clear()
        _screen.value = target
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
