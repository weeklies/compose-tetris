package dev.bildungsroman.tetrominauts.ui

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
                Box(Modifier.padding(vertical = 10.dp)) { Text("About", style = typography.h5) }
            },
            text = {
                Text(
                    text =
                        """
                            Tetrominauts is a spin on the traditional block puzzle game, combining tetrominoes with tetromi(nots). 
                             
                            Controls
                            • Rotate: Tap or Swipe Up
                            • Move: Swipe Left or Right
                            • Drop: Swipe Down
        """
                            .trimIndent(),
                    style = typography.h6
                )
            },
            buttons = {}
        )
}
