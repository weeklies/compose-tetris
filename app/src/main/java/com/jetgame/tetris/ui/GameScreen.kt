package com.jetgame.tetris.ui

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetgame.tetris.logic.*
import com.jetgame.tetris.logic.Direction.*
import com.jetgame.tetris.ui.theme.*
import kotlin.math.absoluteValue
import kotlin.math.min

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewState: GameViewModel.ViewState,
    interactive: Interactive,
) {

    Column(modifier = modifier) {
        GameSettings(
            interactive = interactive,
            isInfoDialogOpen = viewState.isInfoDialogOpen,
            isMute = viewState.isMute,
            isDarkMode = viewState.isDarkMode,
            isPaused = viewState.isPaused,
            gameStatus = viewState.gameStatus
        )

        val animateValue by
            rememberInfiniteTransition()
                .animateFloat(
                    initialValue = 0f,
                    targetValue = 0.7f,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(durationMillis = 1500),
                            repeatMode = RepeatMode.Reverse,
                        ),
                )
        GameScoreboard(
            dropBlock =
                run {
                    if (viewState.dropBlock == DropBlock.Empty) DropBlock.Empty
                    else viewState.dropBlockNext.rotate()
                },
            score = viewState.score,
            line = viewState.line,
            level = viewState.level,
        )

        Spacer(Modifier.height(16.dp))

        var swipeDirection = SwipeDirection.None

        val surface = colors.surface
        val onSurface = colors.onSurface

        Canvas(
            modifier =
                Modifier.fillMaxSize()
                    .pointerInput(viewState.gameStatus) {
                        detectTapGestures(
                            onTap = {
                                when (viewState.gameStatus) {
                                    GameStatus.Onboard,
                                    GameStatus.GameOver -> interactive.onRestart()
                                    GameStatus.Running -> interactive.onRotate()
                                    else -> {}
                                }
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()

                                val minAmount = 10
                                val (x, y) = dragAmount
                                val absX = x.absoluteValue
                                val absY = y.absoluteValue

                                if (absX < minAmount && absY < minAmount) {
                                    // This acts as a buffer against accidental swipes.
                                } else if (absX >= absY) {
                                    // Prioritise horizontal swipes.
                                    when {
                                        x > 0 -> swipeDirection = SwipeDirection.Right
                                        x < 0 -> swipeDirection = SwipeDirection.Left
                                    }
                                } else {
                                    when {
                                        y > 0 -> swipeDirection = SwipeDirection.Down
                                        y < 0 -> swipeDirection = SwipeDirection.Up
                                    }
                                }
                            },
                            onDragEnd = {
                                when (swipeDirection) {
                                    SwipeDirection.Right -> interactive.onMove(Right)
                                    SwipeDirection.Left -> interactive.onMove(Left)
                                    SwipeDirection.Down -> interactive.onMove(Up)
                                    SwipeDirection.Up -> interactive.onRotate()
                                    SwipeDirection.None -> {}
                                }
                                swipeDirection = SwipeDirection.None
                            },
                        )
                    }
        ) {
            val screenWidth = size.width
            val brickSize =
                min(screenWidth / viewState.matrix.first, size.height / viewState.matrix.second)

            // This is used to center the Game Display, along with screenWidth.
            val leftOffset = (screenWidth / brickSize - viewState.matrix.first) / 2

            if (viewState.showGridOutline)
                drawMatrix(
                    brickSize,
                    viewState.matrix,
                    leftOffset,
                    surface,
                )
            drawMatrixBorder(
                brickSize,
                viewState.matrix,
                leftOffset * brickSize,
                onSurface,
            )
            drawBlocks(
                viewState.blocks,
                brickSize,
                viewState.matrix,
                leftOffset,
                viewState.isDarkMode,
            )
            if (viewState.useGhostBlock)
                drawGhostBlock(
                    viewState.ghostBlock,
                    brickSize,
                    viewState.matrix,
                    leftOffset,
                    viewState.isDarkMode,
                )
            drawDropBlock(
                viewState.dropBlock,
                brickSize,
                viewState.matrix,
                leftOffset,
                getColor(viewState.dropBlock.colorIndex, viewState.isDarkMode)
            )
            drawText(
                viewState.gameStatus,
                brickSize,
                viewState.matrix,
                animateValue,
                screenWidth,
                onSurface,
            )
        }
    }
}

@Composable
fun GameSettings(
    interactive: Interactive,
    isInfoDialogOpen: Boolean,
    isMute: Boolean,
    isDarkMode: Boolean,
    isPaused: Boolean,
    gameStatus: GameStatus
) {
    Row {
        // Allow the player to stop running game
        if (gameStatus == GameStatus.Running || gameStatus == GameStatus.Paused) {
            IconButton({ interactive.onPause() }) {
                Icon(
                    if (isPaused) Icons.Rounded.PlayArrow else Icons.Rounded.Pause,
                    if (isPaused) "Resume" else "Pause",
                )
            }
            IconButton({ interactive.onRestart() }) {
                Icon(
                    Icons.Rounded.Stop,
                    "Stop",
                )
            }
        } else {
            IconButton({ interactive.onSettings() }) { Icon(Icons.Rounded.Tune, "Settings") }

            InfoDialog(isInfoDialogOpen) { interactive.onInfo() }
            IconButton({ interactive.onInfo() }) { Icon(Icons.Outlined.Info, "Instructions") }
        }

        Spacer(Modifier.weight(1f))

        IconButton({ interactive.onDarkMode() }) {
            Icon(
                if (!isDarkMode) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                if (!isDarkMode) "Dark Theme" else "Light Theme",
            )
        }
        IconButton({ interactive.onMute() }) {
            Icon(
                if (isMute) Icons.Rounded.VolumeOff else Icons.Rounded.VolumeUp,
                if (isMute) "Sound On" else "Sound Off",
            )
        }
    }
}

@Composable
fun GameScoreboard(
    modifier: Modifier = Modifier,
    dropBlock: DropBlock,
    score: Int = 0,
    line: Int = 0,
    level: Int = 1,
) {
    Column(
        modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Score")
            LedNumber(num = score, digits = 6)

            Text("Lines")
            LedNumber(num = line, digits = 6)

            Text("Level")
            LedNumber(num = level, digits = 1)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(Modifier.size(100.dp, 25.dp)) {
            Text("Next")
            Spacer(modifier = Modifier.width(6.dp))

            val surface = colors.surface
            val onSurface = colors.onSurface

            Canvas(modifier = Modifier.fillMaxSize()) {
                val brickSize = min(size.width / NextMatrix.first, size.height / NextMatrix.second)
                drawMatrix(brickSize, NextMatrix, color = surface)
                drawDropBlock(
                    dropBlock.adjustOffset(NextMatrix),
                    brickSize,
                    NextMatrix,
                    color = onSurface
                )
            }
        }
    }
}

private fun DrawScope.drawText(
    gameStatus: GameStatus,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    alpha: Float,
    screenWidth: Float,
    textColor: Color,
) {

    val centerY = brickSize * matrix.second / 2
    val drawText = { text: String, subtitle: String?, size: Float ->
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                text,
                screenWidth / 2,
                centerY - brickSize,
                Paint().apply {
                    color = textColor.copy(alpha = alpha).toArgb()
                    textSize = size
                    textAlign = Paint.Align.CENTER
                    style = Paint.Style.FILL_AND_STROKE
                    typeface = Typeface.MONOSPACE
                    strokeWidth = (size - 15f) / 12
                }
            )

            if (subtitle != null)
                it.nativeCanvas.drawText(
                    subtitle,
                    screenWidth / 2,
                    centerY + 2 * brickSize,
                    Paint().apply {
                        color = textColor.copy(alpha = alpha).toArgb()
                        textSize = size - 30f
                        textAlign = Paint.Align.CENTER
                        style = Paint.Style.FILL_AND_STROKE
                        typeface = Typeface.MONOSPACE
                        strokeWidth = (size - 50f) / 12
                    }
                )
        }
    }
    if (gameStatus == GameStatus.Onboard) {
        drawText("Tetrominauts!", "Tap to start", 80f)
    } else if (gameStatus == GameStatus.GameOver) {
        drawText("Game Over", "Tap to restart", 80f)
    }
}

private fun DrawScope.drawMatrix(
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f,
    color: Color,
) {
    (0 until matrix.first).forEach { x ->
        (0 until matrix.second).forEach { y ->
            drawBrick(
                brickSize,
                Offset(
                    x.toFloat() + leftOffset,
                    y.toFloat(),
                ),
                color,
            )
        }
    }
}

private fun DrawScope.drawMatrixBorder(
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float,
    color: Color,
) {

    val gap = matrix.first * brickSize * 0.05f
    drawRect(
        color.copy(alpha = 0.4f),
        size = Size(matrix.first * brickSize + gap, matrix.second * brickSize + gap),
        topLeft = Offset(leftOffset - gap / 2, -gap / 2),
        style = Stroke(2.5.dp.toPx())
    )
}

internal fun DrawScope.drawBlocks(
    blocks: List<Block>,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f,
    isDark: Boolean
) {
    clipRect(0f, 0f, bottom = matrix.second * brickSize) {
        blocks.forEach {
            val (x, y) = it.location

            drawBrick(
                brickSize,
                Offset(
                    x + leftOffset,
                    y,
                ),
                getColor(it.colorIndex, isDark)
            )
        }
    }
}

private fun DrawScope.drawGhostBlock(
    dropBlock: DropBlock,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f,
    isDark: Boolean,
) {
    drawDropBlock(
        dropBlock,
        brickSize,
        matrix,
        leftOffset,
        if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.25f)
    )
}

private fun DrawScope.drawDropBlock(
    dropBlock: DropBlock,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f,
    color: Color
) {
    clipRect(0f, 0f, bottom = matrix.second * brickSize) {
        dropBlock.location.forEach {
            drawBrick(
                brickSize,
                Offset(
                    it.x + leftOffset,
                    it.y,
                ),
                color
            )
        }
    }
}

private fun DrawScope.drawBrick(brickSize: Float, offset: Offset, color: Color) {

    val actualLocation = Offset(offset.x * brickSize, offset.y * brickSize)

    val outerSize = brickSize * 0.8f
    val outerOffset = (brickSize - outerSize) / 2

    drawRect(
        color,
        topLeft = actualLocation + Offset(outerOffset, outerOffset),
        size = Size(outerSize, outerSize),
        style = Stroke(outerSize / 10)
    )

    val innerSize = brickSize * 0.6f
    val innerOffset = (brickSize - innerSize) / 2

    drawRect(
        color,
        actualLocation + Offset(innerOffset, innerOffset),
        size = Size(innerSize, innerSize)
    )
}

@Composable
fun PreviewGameScreen(modifier: Modifier = Modifier) {
    GameScreen(
        modifier,
        viewModel<GameViewModel>().viewState.value,
        combinedInteractive(),
    )
}

@Preview
@Composable
fun PreviewDropBlockType() {
    Row(Modifier.size(300.dp, 50.dp).background(md_theme_light_background)) {
        val matrix = 2 to 4
        BlockType.forEach {
            Canvas(Modifier.weight(1f).fillMaxHeight().padding(5.dp)) {
                drawBlocks(
                    Block.of(DropBlock(it).adjustOffset(matrix)),
                    min(size.width / matrix.first, size.height / matrix.second),
                    matrix,
                    isDark = false
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewNautBlockType() {
    Row(Modifier.size(300.dp, 50.dp).background(md_theme_light_background)) {
        val matrix = 3 to 3
        NautBlockType.forEach {
            Canvas(Modifier.weight(1f).fillMaxHeight().padding(5.dp)) {
                drawBlocks(
                    Block.of(DropBlock(it).adjustOffset(matrix)),
                    min(size.width / matrix.first, size.height / matrix.second),
                    matrix,
                    isDark = false
                )
            }
        }
    }
}

fun getColor(colorIndex: Int, isDark: Boolean): Color {
    return if (isDark) darkBlockColors[colorIndex] else lightBlockColors[colorIndex]
}

private enum class SwipeDirection {
    Left,
    Right,
    Up,
    Down,
    None,
}

val lightBlockColors =
    listOf(
        light_onPurpleContainer, // This is the color used for screen clearing.
        light_Green,
        light_Purple,
        light_Yellow,
        light_Orange,
        light_Silver,
        light_Blue,
        light_Red,
    )

val darkBlockColors =
    listOf(
        dark_onPurpleContainer, // This is the color used for screen clearing.
        dark_Green,
        dark_Purple,
        dark_Yellow,
        dark_Orange,
        dark_Silver,
        dark_Blue,
        dark_Red
    )

// Non-adaptive colors
//    Green,
//    Purple,
//    Yellow,
//    Red,
//    Orange,
