package com.jetgame.tetris.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameBody(modifier: Modifier = Modifier, screen: @Composable () -> Unit) {
    // Game Display
    Box(modifier.padding(16.dp).fillMaxWidth().height(600.dp)) { screen() }
}

@Preview(widthDp = 400, heightDp = 700, showBackground = true)
@Composable
fun PreviewGameBody() {
    GameBody {}
}
