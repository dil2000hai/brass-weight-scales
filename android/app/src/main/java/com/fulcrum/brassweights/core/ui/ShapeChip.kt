package com.fulcrum.brassweights.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.theme.BenchSage
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.InkFaint
import com.fulcrum.brassweights.core.theme.Parchment
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.domain.model.ShapeType

@Composable
fun ShapeChip(
    shape: ShapeType,
    count: Int,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bounce by animateFloatAsState(
        targetValue = if (count > 0) 1.06f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 320f),
        label = "chipBounce"
    )
    val selectedState = stringResource(
        if (selected) R.string.state_selected else R.string.state_not_selected
    )
    Box(
        modifier = modifier
            .height(60.dp)
            .graphicsLayer {
                scaleX = bounce
                scaleY = bounce
            }
            .background(
                if (enabled) BenchSage else SageMist.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.5.dp,
                if (selected && enabled) Indigo else SageMist,
                RoundedCornerShape(12.dp)
            )
            .semantics { stateDescription = selectedState }
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        ShapeSprite(
            shape = shape,
            size = 40.dp,
            contentDescription = stringResource(R.string.cd_tray_shape)
        )
        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .size(18.dp)
                    .background(if (enabled) Indigo else InkFaint, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.plain_value, count),
                    style = MaterialTheme.typography.labelSmall,
                    color = Parchment
                )
            }
        }
    }
}
