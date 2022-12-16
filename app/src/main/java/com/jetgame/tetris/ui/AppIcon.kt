package com.jetgame.tetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetgame.tetris.logic.Block
import com.jetgame.tetris.logic.BlockType
import com.jetgame.tetris.logic.DropBlock
import kotlin.math.min

@Preview(widthDp = 400, heightDp = 400) @Composable private fun PreviewAppIcon() = AppIcon()

@Composable
fun AppIcon() {
    Row(Modifier.background(Color(0xFF000000)).padding(40.dp)) {
        val matrix = 2 to 4

        Canvas(Modifier.weight(1f).fillMaxHeight()) {
            drawBlocks(
                Block.of(DropBlock(BlockType[6]).adjustOffset(matrix).copy(colorIndex = 2)),
                min(size.width / matrix.first, size.height / matrix.second),
                matrix,
                isDark = true
            )
        }
    }
}
