package com.jetgame.tetris.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoDialog(isOpen: Boolean, onDismiss: () -> Unit) {
    if (isOpen)
        AlertDialog(
            onDismissRequest = onDismiss,
            backgroundColor = colors.background,
            title = {
                Box(Modifier.padding(vertical = 10.dp)) { Text("Guide", style = typography.h5) }
            },
            text = {
                Text(
                    text =
                        """
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum imperdiet tincidunt molestie. Donec quis tristique leo. 
        """
                            .trimIndent(),
                    style = typography.h6
                )
            },
            buttons = {}
        )
}
