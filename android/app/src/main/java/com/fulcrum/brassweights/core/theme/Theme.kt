package com.fulcrum.brassweights.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BenchColorScheme = lightColorScheme(
    primary = Brass,
    onPrimary = Parchment,
    primaryContainer = BrassLight,
    onPrimaryContainer = BrassDeep,
    secondary = Indigo,
    onSecondary = Parchment,
    secondaryContainer = IndigoSoft,
    onSecondaryContainer = Ink,
    tertiary = BalancedSage,
    onTertiary = Parchment,
    background = BenchSage,
    onBackground = Ink,
    surface = LinenSurface,
    onSurface = Ink,
    surfaceVariant = SageMist,
    onSurfaceVariant = InkMuted,
    outline = SageMist,
    outlineVariant = BenchSageDeep,
    error = TiltTerracotta,
    onError = Parchment
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BenchColorScheme,
        typography = AppTypography,
        content = content
    )
}
