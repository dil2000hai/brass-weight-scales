package com.fulcrum.brassweights.domain.model

data class Deduction(
    val leftCount: Int,
    val leftShape: ShapeType,
    val rightCount: Int,
    val rightShape: ShapeType
)
