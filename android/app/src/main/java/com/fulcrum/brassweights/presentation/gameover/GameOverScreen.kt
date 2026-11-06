package com.fulcrum.brassweights.presentation.gameover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.fulcrum.brassweights.core.theme.BalancedSage
import com.fulcrum.brassweights.core.theme.BenchSage
import com.fulcrum.brassweights.core.theme.BenchSageDeep
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.InkMuted
import com.fulcrum.brassweights.core.theme.TiltTerracotta
import com.fulcrum.brassweights.core.ui.BalanceScale
import com.fulcrum.brassweights.core.ui.GlassCard
import com.fulcrum.brassweights.core.ui.PrimaryButton
import com.fulcrum.brassweights.core.ui.SecondaryButton
import com.fulcrum.brassweights.core.ui.SectionCaption
import com.fulcrum.brassweights.core.ui.StatCard
import com.fulcrum.brassweights.core.ui.shapeNameRes
import com.fulcrum.brassweights.domain.model.ShapeType

private data class ResultStat(val iconRes: Int, val value: String, val label: String, val color: Color)

@Composable
fun GameOverScreen(
    solved: Boolean,
    moves: Int,
    hints: Int,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    val result = state.result

    val appear = remember { MutableTransitionState(false) }
    appear.targetState = true

    val answerCount = result?.answerCount ?: moves
    val answerShape = result?.answerShape ?: ShapeType.TRIANGLE
    val puzzleNumber = result?.puzzleNumber ?: state.solvedCount
    val streak = result?.streak ?: 0

    val stats = buildList {
        if (moves > 0) {
            add(
                ResultStat(
                    iconRes = R.drawable.ic_refresh,
                    value = stringResource(R.string.plain_value, moves),
                    label = stringResource(R.string.stat_moves),
                    color = Brass
                )
            )
        }
        if (hints > 0) {
            add(
                ResultStat(
                    iconRes = R.drawable.ic_bulb,
                    value = stringResource(R.string.plain_value, hints),
                    label = stringResource(R.string.stat_hints),
                    color = Indigo
                )
            )
        }
        if (streak > 0) {
            add(
                ResultStat(
                    iconRes = R.drawable.ic_star,
                    value = stringResource(R.string.plain_value, streak),
                    label = stringResource(R.string.stat_streak),
                    color = TiltTerracotta
                )
            )
        }
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
                            BenchSage.copy(alpha = 0.90f),
                            BenchSageDeep.copy(alpha = 0.95f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(
                visibleState = appear,
                enter = fadeIn(tween(380)) + scaleIn(
                    initialScale = 0.88f,
                    animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f)
                )
            ) {
                BalanceScale(
                    left = result?.deductions?.firstOrNull()?.let { step ->
                        List(step.leftCount) { step.leftShape }
                    } ?: listOf(answerShape),
                    right = result?.deductions?.firstOrNull()?.let { step ->
                        List(step.rightCount) { step.rightShape }
                    } ?: listOf(answerShape),
                    angleDegrees = 0f,
                    glow = if (solved) 0.3f else 0f,
                    beamWidth = 170.dp,
                    panWidth = 96.dp,
                    itemSize = 26.dp,
                    pivotTop = 38.dp,
                    modifier = Modifier.height(160.dp)
                )
            }
            Text(
                text = stringResource(
                    if (solved) R.string.gameover_balanced else R.string.gameover_not_yet
                ),
                style = MaterialTheme.typography.displaySmall,
                color = if (solved) Brass else TiltTerracotta,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (solved) {
                    stringResource(R.string.gameover_solved_sub, puzzleNumber, moves)
                } else {
                    stringResource(R.string.gameover_failed_sub, puzzleNumber)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = InkMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            SectionCaption(
                text = stringResource(R.string.deduction_caption),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    result?.deductions?.take(3)?.forEachIndexed { index, step ->
                        DeductionRow(
                            text = stringResource(
                                R.string.deduction_line,
                                index + 1,
                                step.leftCount,
                                stringResource(shapeNameRes(step.leftShape, step.leftCount)),
                                step.rightCount,
                                stringResource(shapeNameRes(step.rightShape, step.rightCount))
                            )
                        )
                    }
                    DeductionRow(
                        text = stringResource(
                            R.string.deduction_answer,
                            (result?.deductions?.size ?: 0) + 1,
                            answerCount,
                            stringResource(shapeNameRes(answerShape, answerCount))
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            if (stats.size >= 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    stats.forEach { stat ->
                        StatCard(
                            iconRes = stat.iconRes,
                            value = stat.value,
                            label = stat.label,
                            valueColor = stat.color,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                iconRes = R.drawable.ic_refresh,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconRes = R.drawable.ic_arrow_back,
                accent = Brass,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DeductionRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(22.dp)
                .background(BalancedSage, RoundedCornerShape(2.dp))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Ink,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
