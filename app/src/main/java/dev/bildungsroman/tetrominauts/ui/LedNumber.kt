package dev.bildungsroman.tetrominauts.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bildungsroman.tetrominauts.logic.LedFontFamily

@Composable
fun LedNumber(modifier: Modifier = Modifier, num: Int, digits: Int, fillZero: Boolean = false) {
    val textSize = 16.sp
    val textWidth = 8.dp
    Box(modifier.padding(horizontal = 6.dp)) {
        // Hide the background 8 from screen readers
        Row(modifier = Modifier.align(Alignment.CenterEnd).clearAndSetSemantics { disabled() }) {
            repeat(digits) {
                Text(
                    "8",
                    fontSize = textSize,
                    color = MaterialTheme.colors.surface,
                    fontFamily = LedFontFamily,
                    modifier = Modifier.width(textWidth),
                    textAlign = TextAlign.End
                )
            }
        }
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
        ) {
            val str = if (fillZero) String.format("%0${digits}d", num) else num.toString()
            str.iterator().forEach {
                Text(
                    it.toString(),
                    fontSize = textSize,
                    color = MaterialTheme.colors.onSurface,
                    fontFamily = LedFontFamily,
                    modifier = Modifier.width(textWidth),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
