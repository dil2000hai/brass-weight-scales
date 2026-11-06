package com.fulcrum.brassweights.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.BrassDeep
import com.fulcrum.brassweights.core.theme.BrassLight
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.domain.model.ClueScale

@Composable
fun ClueScaleCard(
    clue: ClueScale,
    showEquation: Boolean,
    highlighted: Boolean,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 14.dp,
        elevation = 3.dp,
        borderColor = if (highlighted) Indigo else SageMist
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            if (showEquation) {
                Text(
                    text = stringResource(
                        R.string.clue_equation,
                        clue.left.size,
                        stringResource(shapeLetterRes(clue.left.first())),
                        clue.right.size,
                        stringResource(shapeLetterRes(clue.right.first()))
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink,
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ClueGroup(clue.left)
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .width(14.dp)
                                .height(3.dp)
                                .background(Indigo.copy(alpha = 0.55f), RoundedCornerShape(2.dp))
                        )
                        ClueGroup(clue.right)
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(
                                Brush.linearGradient(listOf(BrassLight, Brass, BrassDeep)),
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ClueGroup(items: List<com.fulcrum.brassweights.domain.model.ShapeType>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items.take(5).forEach { shape ->
            ShapeSprite(
                shape = shape,
                size = 20.dp,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 1.dp)
            )
        }
    }
}
