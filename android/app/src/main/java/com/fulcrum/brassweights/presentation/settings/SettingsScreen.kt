package com.fulcrum.brassweights.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.di.ServiceLocator
import com.fulcrum.brassweights.core.theme.BenchSage
import com.fulcrum.brassweights.core.theme.BenchSageDeep
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.BrassLight
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.InkMuted
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.theme.Parchment
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.core.theme.TiltTerracotta
import com.fulcrum.brassweights.core.ui.GlassCard
import com.fulcrum.brassweights.core.ui.SecondaryButton
import com.fulcrum.brassweights.core.ui.SectionCaption
import com.fulcrum.brassweights.core.ui.gradeNameRes
import com.fulcrum.brassweights.domain.model.GradeLevel
import com.fulcrum.brassweights.presentation.dialog.ResetConfirmDialog

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    if (state.resetVisible) {
        ResetConfirmDialog(
            onConfirm = { settingsViewModel.onResetConfirmed() },
            onDismiss = { settingsViewModel.onResetDismissed() }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            BenchSage.copy(alpha = 0.93f),
                            BenchSageDeep.copy(alpha = 0.97f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Surface(
                color = LinenSurface.copy(alpha = 0.82f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.cd_back),
                            tint = Ink,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleMedium,
                        color = Ink,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    )
                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SageMist)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SwitchRow(
                    label = stringResource(R.string.settings_sound),
                    checked = state.settings.sound,
                    onCheckedChange = { settingsViewModel.onSoundChanged(it) }
                )
                SwitchRow(
                    label = stringResource(R.string.settings_haptics),
                    checked = state.settings.haptics,
                    onCheckedChange = { settingsViewModel.onHapticsChanged(it) }
                )
                SwitchRow(
                    label = stringResource(R.string.settings_symbols),
                    checked = state.settings.showSymbols,
                    onCheckedChange = { settingsViewModel.onSymbolsChanged(it) }
                )
                SectionCaption(text = stringResource(R.string.settings_grade))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GradeLevel.entries.forEach { grade ->
                        SecondaryButton(
                            text = stringResource(gradeNameRes(grade)),
                            onClick = { settingsViewModel.onGradeChanged(grade) },
                            accent = if (state.settings.gradeLevel == grade) Brass else InkMuted,
                            height = 46.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                SectionCaption(text = stringResource(R.string.teacher_caption))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.teacher_note_one),
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMuted
                        )
                        Text(
                            text = stringResource(R.string.teacher_note_two),
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMuted
                        )
                        Text(
                            text = stringResource(R.string.teacher_note_three),
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMuted
                        )
                    }
                }
                SecondaryButton(
                    text = stringResource(R.string.reset_progress),
                    onClick = { settingsViewModel.onResetRequested() },
                    iconRes = R.drawable.ic_close,
                    accent = TiltTerracotta,
                    enabled = state.solvedCount > 0,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val stateLabel = stringResource(
        if (checked) R.string.state_selected else R.string.state_not_selected
    )
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 14.dp,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = Ink,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Parchment,
                    checkedTrackColor = Brass,
                    checkedBorderColor = BrassLight,
                    uncheckedThumbColor = InkMuted,
                    uncheckedTrackColor = SageMist,
                    uncheckedBorderColor = BenchSageDeep
                ),
                modifier = Modifier.semantics { stateDescription = stateLabel }
            )
        }
    }
}
