package com.fulcrum.brassweights.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fulcrum.brassweights.core.theme.BalancedSage
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.BrassDeep
import com.fulcrum.brassweights.core.theme.BrassLight
import com.fulcrum.brassweights.core.theme.BrassSpecular
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.core.theme.TiltTerracotta
import com.fulcrum.brassweights.domain.model.ShapeType
import kotlin.math.cos
import kotlin.math.sin

private val brassBrush = Brush.linearGradient(listOf(BrassLight, Brass, BrassDeep))

@Composable
fun ShapeSprite(
    shape: ShapeType,
    size: Dp,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Image(
        painter = painterResource(shapeDrawable(shape)),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(size)
    )
}

@Composable
fun BalanceScale(
    left: List<ShapeType>,
    right: List<ShapeType>,
    angleDegrees: Float,
    modifier: Modifier = Modifier,
    glow: Float = 0f,
    beamWidth: Dp = 190.dp,
    panWidth: Dp = 108.dp,
    itemSize: Dp = 30.dp,
    pivotTop: Dp = 46.dp,
    leftHighlight: Boolean = false,
    rightHighlight: Boolean = false,
    leftDescription: String? = null,
    rightDescription: String? = null,
    onLeftPanClick: (() -> Unit)? = null,
    onRightPanClick: (() -> Unit)? = null
) {
    val radians = angleDegrees * 0.017453292f
    val half = beamWidth.value / 2f
    val offsetX = (half * cos(radians)).dp
    val offsetY = (half * sin(radians)).dp

    Box(modifier = modifier.fillMaxWidth()) {
        if (glow > 0f) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = pivotTop - 26.dp)
                    .size(104.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                BalancedSage.copy(alpha = glow),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = pivotTop + 54.dp)
                .width(86.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(brassBrush)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = pivotTop)
                .width(18.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .background(brassBrush)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = pivotTop - 5.dp)
                .width(beamWidth)
                .height(10.dp)
                .graphicsLayer { rotationZ = angleDegrees }
                .clip(RoundedCornerShape(5.dp))
                .background(brassBrush)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(BrassSpecular.copy(alpha = 0.55f))
            )
        }
        Pan(
            items = left,
            panWidth = panWidth,
            itemSize = itemSize,
            highlight = leftHighlight,
            description = leftDescription,
            onClick = onLeftPanClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = -offsetX, y = pivotTop - offsetY)
        )
        Pan(
            items = right,
            panWidth = panWidth,
            itemSize = itemSize,
            highlight = rightHighlight,
            description = rightDescription,
            onClick = onRightPanClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = offsetX, y = pivotTop + offsetY)
        )
    }
}

@Composable
private fun Pan(
    items: List<ShapeType>,
    panWidth: Dp,
    itemSize: Dp,
    highlight: Boolean,
    description: String?,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val borderColor = if (highlight) TiltTerracotta else Color.Transparent
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(1.5.dp)
                .height(20.dp)
                .background(BrassDeep)
        )
        Column(
            modifier = Modifier
                .width(panWidth)
                .heightIn(min = if (onClick != null) 48.dp else 36.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.5.dp, borderColor, RoundedCornerShape(10.dp))
                .then(
                    if (description != null) {
                        Modifier.semantics { contentDescription = description }
                    } else {
                        Modifier
                    }
                )
                .then(
                    if (onClick != null) Modifier.clickable { onClick() } else Modifier
                )
                .padding(vertical = 3.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            items.chunked(3).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.Center) {
                    rowItems.forEach { shape ->
                        ShapeSprite(
                            shape = shape,
                            size = itemSize,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 1.dp, vertical = 1.dp)
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .width(panWidth)
                .height(9.dp)
                .clip(RoundedCornerShape(bottomStart = 9.dp, bottomEnd = 9.dp))
                .background(brassBrush)
                .border(
                    1.dp,
                    SageMist.copy(alpha = 0.4f),
                    RoundedCornerShape(bottomStart = 9.dp, bottomEnd = 9.dp)
                )
        )
    }
}
