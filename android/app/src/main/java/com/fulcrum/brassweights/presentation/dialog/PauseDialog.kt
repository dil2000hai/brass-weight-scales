package com.fulcrum.brassweights.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fulcrum.brassweights.R
import com.fulcrum.brassweights.core.theme.Brass
import com.fulcrum.brassweights.core.theme.Ink
import com.fulcrum.brassweights.core.theme.LinenSurface
import com.fulcrum.brassweights.core.theme.TiltTerracotta
import com.fulcrum.brassweights.core.ui.PrimaryButton
import com.fulcrum.brassweights.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    onGiveUp: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = LinenSurface,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.titleMedium,
                color = Ink
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryButton(
                    text = stringResource(R.string.resume),
                    onClick = onResume,
                    iconRes = R.drawable.ic_play,
                    height = 52.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    text = stringResource(R.string.restart),
                    onClick = onRestart,
                    iconRes = R.drawable.ic_refresh,
                    accent = Brass,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    iconRes = R.drawable.ic_arrow_back,
                    accent = Ink,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onGiveUp) {
                Text(
                    text = stringResource(R.string.give_up),
                    style = MaterialTheme.typography.labelLarge,
                    color = TiltTerracotta
                )
            }
        }
    )
}
