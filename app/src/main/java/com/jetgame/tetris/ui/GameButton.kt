package com.jetgame.tetris.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.jetgame.tetris.ui.theme.Purple200

@Composable
fun GameButton(
    modifier: Modifier = Modifier,
    size: Dp,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier.size(size = size),
        colors = ButtonDefaults.buttonColors(Purple200),
        shape = RoundedCornerShape(size / 2),
        onClick = onClick,
        elevation = ButtonDefaults.elevation(),
    ) {}
}
