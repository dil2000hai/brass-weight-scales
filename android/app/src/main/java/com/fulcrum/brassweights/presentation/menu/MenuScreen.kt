package com.fulcrum.brassweights.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.di.ServiceLocator
import com.fulcrum.brassweights.core.theme.BalancedSage
import com.fulcrum.brassweights.core.theme.BenchSage
import com.fulcrum.brassweights.core.theme.BenchSageDeep
import com.fulcrum.brassweights.core.theme.BenchSageMid
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.BrassDeep
import com.fulcrum.brassweights.core.theme.BrassLight
import com.fulcrum.brassweights.core.theme.BrassMid
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.InkMuted
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.theme.Parchment
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.core.theme.TiltTerracotta
import com.fulcrum.brassweights.core.ui.GlassCard
import com.fulcrum.brassweights.core.ui.SectionCaption
import com.fulcrum.brassweights.core.ui.StatCard
import com.fulcrum.brassweights.core.ui.chapterNameRes
import com.fulcrum.brassweights.presentation.dialog.SkillsDialog

private data class MenuStat(val iconRes: Int, val value: String, val label: String, val color: Color)

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onFreeScale: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    if (state.skillsVisible) {
        SkillsDialog(
            mastery = state.skillMastery,
            onDismiss = { menuViewModel.onSkillsDismiss() }
        )
    }

    val stats = buildList {
        if (state.bestStreak > 0) {
            add(
                MenuStat(
                    iconRes = R.drawable.ic_star,
                    value = stringResource(R.string.plain_value, state.bestStreak),
                    label = stringResource(R.string.best_streak),
                    color = TiltTerracotta
                )
            )
        }
        if (state.masteryPercent > 0) {
            add(
                MenuStat(
                    iconRes = R.drawable.ic_check,
                    value = stringResource(R.string.percent_value, state.masteryPercent),
                    label = stringResource(R.string.mastery),
                    color = BalancedSage
                )
            )
        }
        if (state.hintsUsed > 0) {
            add(
                MenuStat(
                    iconRes = R.drawable.ic_bulb,
                    value = stringResource(R.string.plain_value, state.hintsUsed),
                    label = stringResource(R.string.hints_used),
                    color = Indigo
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
                            BenchSage.copy(alpha = 0.94f),
                            BenchSageMid.copy(alpha = 0.96f),
                            BenchSageDeep.copy(alpha = 0.97f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(260.dp)
                .background(
                    Brush.radialGradient(listOf(Brass.copy(alpha = 0.14f), Color.Transparent))
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            MenuHeader(onSettings = onSettings)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(SageMist)
            )
            Spacer(modifier = Modifier.height(14.dp))
            ProgressBadge(solved = state.solvedCount, total = state.total)
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayTile(
                    chapter = stringResource(chapterNameRes(state.chapter)),
                    puzzleInChapter = state.puzzleInChapter,
                    onPlay = onPlay,
                    modifier = Modifier
                        .weight(1.55f)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (stats.size >= 2) {
                        stats.forEach { stat ->
                            StatCard(
                                iconRes = stat.iconRes,
                                value = stat.value,
                                label = stat.label,
                                valueColor = stat.color,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                    } else {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            borderColor = SageMist
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.icon_home),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = stringResource(R.string.menu_hint_card),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = InkMuted,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FlatTile(
                    label = stringResource(R.string.free_scale),
                    iconRes = R.drawable.ic_scale,
                    container = LinenSurface,
                    border = Indigo,
                    contentColor = Indigo,
                    onClick = onFreeScale,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                FlatTile(
                    label = stringResource(R.string.skills),
                    iconRes = R.drawable.ic_check,
                    container = SageMist,
                    border = BenchSageDeep,
                    contentColor = InkMuted,
                    onClick = { menuViewModel.onSkillsOpen() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            SectionCaption(
                text = stringResource(R.string.menu_footer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun MenuHeader(onSettings: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(LinenSurface)
                .border(1.5.dp, BrassLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_home),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = stringResource(R.string.splash_title),
            style = MaterialTheme.typography.titleLarge,
            color = Ink,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        IconButton(
            onClick = onSettings,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_tuner),
                contentDescription = stringResource(R.string.cd_settings),
                tint = Brass,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun ProgressBadge(solved: Int, total: Int) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        cornerRadius = 14.dp,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = null,
                tint = Brass,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.puzzles_solved),
                style = MaterialTheme.typography.labelSmall,
                color = InkMuted,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.solved_value, solved, total),
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun PlayTile(
    chapter: String,
    puzzleInChapter: Int,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "playTile")
    val breathe by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.016f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )
    val shape = RoundedCornerShape(16.dp)
    Button(
        onClick = onPlay,
        shape = shape,
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
        modifier = modifier
            .graphicsLayer {
                scaleX = breathe
                scaleY = breathe
            }
            .border(1.dp, BrassLight, shape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(listOf(BrassMid, Brass, BrassDeep)),
                    shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(68.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.chapter_puzzle, chapter, puzzleInChapter),
                    style = MaterialTheme.typography.labelSmall,
                    color = Parchment.copy(alpha = 0.82f),
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_play),
                        contentDescription = null,
                        tint = Parchment,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.play),
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp),
                        color = Parchment
                    )
                }
            }
        }
    }
}

@Composable
private fun FlatTile(
    label: String,
    iconRes: Int,
    container: Color,
    border: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = container),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp),
        modifier = modifier.border(1.5.dp, border, shape)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
