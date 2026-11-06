package com.fulcrum.brassweights.domain.model

data class BalanceState(
    val leftSum: Int,
    val rightSum: Int,
    val angleDegrees: Float,
    val solved: Boolean
)
