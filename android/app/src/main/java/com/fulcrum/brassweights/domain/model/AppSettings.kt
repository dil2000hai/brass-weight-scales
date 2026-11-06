package com.fulcrum.brassweights.domain.model

enum class GradeLevel {
    EARLY,
    MIDDLE,
    ADVANCED
}

data class AppSettings(
    val sound: Boolean = true,
    val haptics: Boolean = true,
    val showSymbols: Boolean = false,
    val gradeLevel: GradeLevel = GradeLevel.MIDDLE
)
