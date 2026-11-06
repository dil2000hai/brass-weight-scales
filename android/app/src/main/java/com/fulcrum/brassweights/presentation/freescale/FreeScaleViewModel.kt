package com.fulcrum.brassweights.presentation.freescale

import androidx.lifecycle.ViewModel
import com.fulcrum.brassweights.core.config.GameConfig
import com.fulcrum.brassweights.domain.model.ShapeType
import com.fulcrum.brassweights.domain.usecase.EvaluateBalanceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FreeScaleViewModel(
    private val evaluateBalance: EvaluateBalanceUseCase,
    private val weights: Map<ShapeType, Int>,
    tray: List<ShapeType>
) : ViewModel() {

    private val _state = MutableStateFlow(FreeScaleUiState(tray = tray))
    val state: StateFlow<FreeScaleUiState> = _state.asStateFlow()

    fun onSideSelected(side: PanSide) {
        _state.update { it.copy(activeSide = side) }
    }

    fun onTrayTapped(shape: ShapeType) {
        val current = _state.value
        if (current.activeSide == PanSide.LEFT) {
            if (current.leftPan.size >= GameConfig.MAX_PER_PAN) return
            apply(current.leftPan + shape, current.rightPan)
        } else {
            if (current.rightPan.size >= GameConfig.MAX_PER_PAN) return
            apply(current.leftPan, current.rightPan + shape)
        }
    }

    fun onRemoveLeft() {
        val current = _state.value
        if (current.leftPan.isEmpty()) return
        apply(current.leftPan.dropLast(1), current.rightPan)
    }

    fun onRemoveRight() {
        val current = _state.value
        if (current.rightPan.isEmpty()) return
        apply(current.leftPan, current.rightPan.dropLast(1))
    }

    fun onClear() {
        apply(emptyList(), emptyList())
    }

    private fun apply(left: List<ShapeType>, right: List<ShapeType>) {
        val balance = evaluateBalance.tilt(left, right, weights)
        _state.update {
            it.copy(
                leftPan = left,
                rightPan = right,
                angle = balance.angleDegrees,
                leftSum = balance.leftSum,
                rightSum = balance.rightSum
            )
        }
    }
}
