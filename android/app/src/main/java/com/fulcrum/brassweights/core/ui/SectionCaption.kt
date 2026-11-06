package com.fulcrum.brassweights.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fulcrum.brassweights.core.theme.InkFaint

@Composable
fun SectionCaption(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = InkFaint,
        modifier = modifier
    )
}
