package com.fulcrum.brassweights.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.theme.BalancedSage
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.InkMuted
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.theme.SageMist
import com.fulcrum.brassweights.core.ui.skillNameRes
import com.fulcrum.brassweights.domain.model.SkillTag

private const val SKILL_TARGET = 35f

@Composable
fun SkillsDialog(
    mastery: Map<SkillTag, Int>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LinenSurface,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.skills_title),
                style = MaterialTheme.typography.titleMedium,
                color = Ink
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SkillTag.entries.forEach { skill ->
                    val solved = mastery[skill] ?: 0
                    val ratio = (solved / SKILL_TARGET).coerceIn(0f, 1f)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(skillNameRes(skill)),
                                style = MaterialTheme.typography.labelSmall,
                                color = InkMuted,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = stringResource(
                                    R.string.percent_value,
                                    (ratio * 100f).toInt()
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = Brass
                            )
                        }
                        LinearProgressIndicator(
                            progress = { ratio },
                            color = BalancedSage,
                            trackColor = SageMist,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    color = Brass
                )
            }
        }
    )
}
