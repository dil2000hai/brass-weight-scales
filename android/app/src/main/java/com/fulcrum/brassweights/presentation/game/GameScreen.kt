package com.fulcrum.brassweights.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.di.ServiceLocator
import com.fulcrum.brassweights.core.theme.BenchSage
import com.fulcrum.brassweights.core.theme.BenchSageDeep
import com.fulcrum.brassweights.core.theme.BenchSageMid
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.InkMuted
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.theme.LinenSurfaceAlt
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.core.ui.BalanceScale
import com.fulcrum.brassweights.core.ui.ClueScaleCard
import com.fulcrum.brassweights.core.ui.PrimaryButton
import com.fulcrum.brassweights.core.ui.SecondaryButton
import com.fulcrum.brassweights.core.ui.SectionCaption
import com.fulcrum.brassweights.core.ui.ShapeChip
import com.fulcrum.brassweights.core.ui.chapterNameRes
import com.fulcrum.brassweights.core.ui.shapeNameRes
import com.fulcrum.brassweights.presentation.dialog.HintDialog
import com.fulcrum.brassweights.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    onFinished: (Boolean, Int, Int) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current

    DisposableEffect(Unit) {
        gameViewModel.onScreenEnter()
        onDispose { gameViewModel.pause() }
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                GameEvent.Clink ->
                    haptics.performHapticFeedback(
                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                    )

                is GameEvent.Finished -> onFinished(event.solved, event.moves, event.hints)
            }
        }
    }

    val puzzle = state.puzzle
    val beamAngle by animateFloatAsState(
        targetValue = state.angle,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 170f),
        label = "beamAngle"
    )
    val glow by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.SOLVED) 0.55f else 0f,
        animationSpec = tween(250),
        label = "solveGlow"
    )

    if (state.hintVisible && puzzle != null) {
        HintDialog(
            deductions = state.deductions,
            revealed = state.revealedHints,
            answerCount = puzzle.optimalMoves,
            answerShape = puzzle.targetShape,
            showEquation = state.showEquation,
            onToggleEquation = { gameViewModel.onToggleEquation() },
            onDismiss = { gameViewModel.onHintDismissed() }
        )
    }
    if (state.pauseVisible) {
        PauseDialog(
            onResume = { gameViewModel.onPauseDismissed() },
            onRestart = { gameViewModel.onRestart() },
            onMenu = onMenu,
            onGiveUp = { gameViewModel.onGiveUp() },
            onDismiss = { gameViewModel.onPauseDismissed() }
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
                            BenchSageMid.copy(alpha = 0.90f),
                            BenchSage.copy(alpha = 0.93f),
                            BenchSageDeep.copy(alpha = 0.96f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                title = if (puzzle == null) {
                    stringResource(R.string.app_name)
                } else {
                    stringResource(
                        R.string.game_header,
                        stringResource(chapterNameRes(puzzle.chapter)),
                        state.puzzleInChapter,
                        state.chapterSize
                    )
                },
                onBack = onMenu,
                onPause = { gameViewModel.onPauseOpened() }
            )
            if (puzzle != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionCaption(text = stringResource(R.string.clue_caption))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        puzzle.clues.take(3).forEachIndexed { index, clue ->
                            ClueScaleCard(
                                clue = clue,
                                showEquation = state.showEquation,
                                highlighted = index < state.revealedHints,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        BalanceScale(
                            left = puzzle.leftPan,
                            right = state.rightPan,
                            angleDegrees = beamAngle,
                            glow = glow,
                            leftHighlight = state.phase == GamePhase.UNBALANCED &&
                                state.tilt == TiltSide.LEFT,
                            rightHighlight = state.phase == GamePhase.UNBALANCED &&
                                state.tilt == TiltSide.RIGHT,
                            leftDescription = stringResource(R.string.cd_left_pan),
                            rightDescription = stringResource(R.string.cd_right_pan),
                            onRightPanClick = { gameViewModel.onRemoveLast() },
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .height(190.dp)
                        )
                    }
                    Text(
                        text = stringResource(
                            R.string.question,
                            stringResource(
                                shapeNameRes(puzzle.targetShape, puzzle.optimalMoves)
                            )
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = Indigo,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    StatStrip(
                        moves = state.moves,
                        hints = state.hints,
                        tilt = state.tilt
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                ControlsBlock(
                    trayShapes = puzzle.trayShapes,
                    rightPan = state.rightPan,
                    selected = state.selectedShape,
                    enabled = state.interactive,
                    onTrayTap = { shape ->
                        gameViewModel.onTraySelected(shape)
                        gameViewModel.onTrayTapped(shape)
                    },
                    onHint = { gameViewModel.onHintRequested() },
                    onCheck = { gameViewModel.onCheck() },
                    onUndo = { gameViewModel.onUndoAll() }
                )
            }
        }
    }
}

@Composable
private fun GameHeader(
    title: String,
    onBack: () -> Unit,
    onPause: () -> Unit
) {
    Surface(
        color = LinenSurface.copy(alpha = 0.82f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
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
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                )
                IconButton(onClick = onPause, modifier = Modifier.size(48.dp)) {
                    Image(
                        painter = painterResource(R.drawable.icon_pause),
                        contentDescription = stringResource(R.string.cd_pause),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SageMist)
            )
        }
    }
}

@Composable
private fun StatStrip(moves: Int, hints: Int, tilt: TiltSide) {
    Surface(
        color = SageMist.copy(alpha = 0.55f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatReading(
                label = stringResource(R.string.stat_moves),
                value = stringResource(R.string.plain_value, moves),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(22.dp)
                    .background(BenchSageDeep)
            )
            StatReading(
                label = stringResource(R.string.stat_hints),
                value = stringResource(R.string.plain_value, hints),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(22.dp)
                    .background(BenchSageDeep)
            )
            StatReading(
                label = stringResource(R.string.stat_tilt),
                value = stringResource(
                    when (tilt) {
                        TiltSide.NONE -> R.string.tilt_level
                        TiltSide.LEFT -> R.string.tilt_left
                        TiltSide.RIGHT -> R.string.tilt_right
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun StatReading(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = InkMuted
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = Ink
        )
    }
}

@Composable
private fun ControlsBlock(
    trayShapes: List<com.fulcrum.brassweights.domain.model.ShapeType>,
    rightPan: List<com.fulcrum.brassweights.domain.model.ShapeType>,
    selected: com.fulcrum.brassweights.domain.model.ShapeType?,
    enabled: Boolean,
    onTrayTap: (com.fulcrum.brassweights.domain.model.ShapeType) -> Unit,
    onHint: () -> Unit,
    onCheck: () -> Unit,
    onUndo: () -> Unit
) {
    Surface(
        color = LinenSurfaceAlt,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        shadowElevation = 10.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            SectionCaption(text = stringResource(R.string.tray_caption))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                trayShapes.take(5).forEach { shape ->
                    ShapeChip(
                        shape = shape,
                        count = rightPan.count { it == shape },
                        selected = selected == shape,
                        enabled = enabled,
                        onClick = { onTrayTap(shape) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    text = stringResource(R.string.hint),
                    onClick = onHint,
                    iconRes = R.drawable.ic_bulb,
                    accent = Indigo,
                    enabled = enabled,
                    modifier = Modifier.width(132.dp)
                )
                PrimaryButton(
                    text = stringResource(R.string.check),
                    onClick = onCheck,
                    iconRes = R.drawable.ic_check,
                    enabled = enabled && rightPan.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            SecondaryButton(
                text = stringResource(R.string.undo),
                onClick = onUndo,
                iconRes = R.drawable.ic_refresh,
                accent = Brass,
                enabled = enabled && rightPan.isNotEmpty(),
                height = 42.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
