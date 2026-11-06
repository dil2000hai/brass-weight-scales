package com.fulcrum.brassweights.presentation.freescale

import com.fulcrum.brassweights.domain.model.ShapeType

enum class PanSide {
    LEFT,
    RIGHT
}

data class FreeScaleUiState(
    val tray: List<ShapeType> = emptyList(),
    val leftPan: List<ShapeType> = emptyList(),
    val rightPan: List<ShapeType> = emptyList(),
    val angle: Float = 0f,
    val leftSum: Int = 0,
    val rightSum: Int = 0,
    val activeSide: PanSide = PanSide.RIGHT
)
