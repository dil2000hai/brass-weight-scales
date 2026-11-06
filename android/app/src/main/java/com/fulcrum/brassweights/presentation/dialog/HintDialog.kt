package com.fulcrum.brassweights.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.theme.Indigo
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.ui.shapeNameRes
import com.fulcrum.brassweights.domain.model.Deduction
import com.fulcrum.brassweights.domain.model.ShapeType

@Composable
fun HintDialog(
    deductions: List<Deduction>,
    revealed: Int,
    answerCount: Int,
    answerShape: ShapeType,
    showEquation: Boolean,
    onToggleEquation: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LinenSurface,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.hint_title),
                style = MaterialTheme.typography.titleMedium,
                color = Ink
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                deductions.take(revealed).forEachIndexed { index, step ->
                    HintLine(
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
                if (revealed > deductions.size) {
                    HintLine(
                        text = stringResource(
                            R.string.deduction_answer,
                            deductions.size + 1,
                            answerCount,
                            stringResource(shapeNameRes(answerShape, answerCount))
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.got_it),
                    style = MaterialTheme.typography.labelLarge,
                    color = Indigo
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onToggleEquation) {
                Text(
                    text = stringResource(
                        if (showEquation) R.string.hide_equation else R.string.show_equation
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = Ink
                )
            }
        }
    )
}

@Composable
private fun HintLine(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(28.dp)
                .background(Indigo, RoundedCornerShape(2.dp))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Ink,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}
