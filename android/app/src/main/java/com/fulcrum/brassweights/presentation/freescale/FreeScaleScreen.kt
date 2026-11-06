package com.fulcrum.brassweights.presentation.freescale

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.fulcrum.brassweights.core.theme.BenchSage
import com.fulcrum.brassweights.core.theme.BenchSageDeep
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.InkMuted
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.theme.LinenSurfaceAlt
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.core.ui.BalanceScale
import com.fulcrum.brassweights.core.ui.PrimaryButton
import com.fulcrum.brassweights.core.ui.SecondaryButton
import com.fulcrum.brassweights.core.ui.SectionCaption
import com.fulcrum.brassweights.core.ui.ShapeChip

@Composable
fun FreeScaleScreen(onBack: () -> Unit) {
    val freeScaleViewModel: FreeScaleViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by freeScaleViewModel.state.collectAsStateWithLifecycle()

    val beamAngle by animateFloatAsState(
        targetValue = state.angle,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 170f),
        label = "freeBeam"
    )

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
                            BenchSage.copy(alpha = 0.92f),
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
                        text = stringResource(R.string.free_scale),
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
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                SectionCaption(
                    text = stringResource(R.string.free_scale_hint),
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BalanceScale(
                        left = state.leftPan,
                        right = state.rightPan,
                        angleDegrees = beamAngle,
                        leftDescription = stringResource(R.string.cd_left_pan),
                        rightDescription = stringResource(R.string.cd_right_pan),
                        onLeftPanClick = { freeScaleViewModel.onRemoveLeft() },
                        onRightPanClick = { freeScaleViewModel.onRemoveRight() },
                        modifier = Modifier.height(200.dp)
                    )
                }
                Surface(
                    color = SageMist.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.stat_tilt),
                            style = MaterialTheme.typography.labelSmall,
                            color = InkMuted
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(
                                when {
                                    state.leftSum > state.rightSum -> R.string.tilt_left
                                    state.rightSum > state.leftSum -> R.string.tilt_right
                                    else -> R.string.tilt_level
                                }
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = Ink,
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
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
                    SectionCaption(text = stringResource(R.string.free_scale_caption))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        state.tray.take(5).forEach { shape ->
                            ShapeChip(
                                shape = shape,
                                count = state.leftPan.count { it == shape } +
                                    state.rightPan.count { it == shape },
                                selected = state.activeSide == PanSide.RIGHT,
                                enabled = true,
                                onClick = { freeScaleViewModel.onTrayTapped(shape) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(R.string.tilt_left),
                            onClick = { freeScaleViewModel.onSideSelected(PanSide.LEFT) },
                            accent = if (state.activeSide == PanSide.LEFT) Indigo else InkMuted,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(R.string.tilt_right),
                            onClick = { freeScaleViewModel.onSideSelected(PanSide.RIGHT) },
                            accent = if (state.activeSide == PanSide.RIGHT) Indigo else InkMuted,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = stringResource(R.string.clear_pans),
                        onClick = { freeScaleViewModel.onClear() },
                        iconRes = R.drawable.ic_close,
                        enabled = state.leftPan.isNotEmpty() || state.rightPan.isNotEmpty(),
                        height = 52.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
