package com.jetgame.tetris.ui

import android.graphics.Paint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetgame.tetris.R
import com.jetgame.tetris.logic.*
import com.jetgame.tetris.ui.theme.BrickMatrix
import com.jetgame.tetris.ui.theme.BrickSpirit
import com.jetgame.tetris.ui.theme.ScreenBackground
import kotlin.math.min
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@Preview(showBackground = true)
@Composable
fun GameScreen(modifier: Modifier = Modifier) {

    val viewModel = viewModel<GameViewModel>()
    val viewState = viewModel.viewState.value

    Column(modifier = modifier) {
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
            spirit =
                run {
                    if (viewState.spirit == Spirit.Empty) Spirit.Empty
                    else viewState.spiritNext.rotate()
                },
            score = viewState.score,
            line = viewState.line,
            level = viewState.level,
            isMute = viewState.isMute,
            isPaused = viewState.isPaused,
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val screenWidth = size.width
            val brickSize =
                min(screenWidth / viewState.matrix.first, size.height / viewState.matrix.second)

            // This is used to center the Game Display, along with screenWidth.
            val leftOffset = (screenWidth / brickSize - viewState.matrix.first) / 2

            drawMatrix(brickSize, viewState.matrix, leftOffset)
            drawMatrixBorder(brickSize, viewState.matrix, leftOffset * brickSize)
            drawBricks(viewState.bricks, brickSize, viewState.matrix, leftOffset)
            drawSpirit(viewState.spirit, brickSize, viewState.matrix, leftOffset)
            drawText(viewState.gameStatus, brickSize, viewState.matrix, animateValue, screenWidth)
        }
    }
}

@Composable
fun GameScoreboard(
    modifier: Modifier = Modifier,
    spirit: Spirit,
    score: Int = 0,
    line: Int = 0,
    level: Int = 1,
    isMute: Boolean = false,
    isPaused: Boolean = false
) {
    Column(
        modifier.fillMaxWidth().padding(vertical = 10.dp),
    ) {
        Row {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_music_off_24),
                colorFilter = ColorFilter.tint(if (isMute) BrickSpirit else BrickMatrix),
                contentDescription = null
            )
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_pause_24),
                colorFilter = ColorFilter.tint(if (isPaused) BrickSpirit else BrickMatrix),
                contentDescription = null
            )
        }

        val margin = 12.dp
        Spacer(modifier = Modifier.width(margin))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val textSize = 12.sp
            Text("Score", fontSize = textSize)
            LedNumber(num = score, digits = 6)

            Text("Lines", fontSize = textSize)
            LedNumber(num = line, digits = 6)

            Text("Level", fontSize = textSize)
            LedNumber(num = level, digits = 1)

            Text("Next", fontSize = textSize)
            Spacer(modifier = Modifier.width(margin))

            Canvas(modifier = Modifier.fillMaxWidth().align(Alignment.Top)) {
                val brickSize = size.width / NextMatrix.first

                drawMatrix(brickSize, NextMatrix)
                drawSpirit(spirit.adjustOffset(NextMatrix), brickSize, NextMatrix)
            }
        }
    }
}

private fun DrawScope.drawText(
    gameStatus: GameStatus,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    alpha: Float,
    screenWidth: Float
) {

    val centerY = brickSize * matrix.second / 2
    val drawText = { text: String, size: Float ->
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                text,
                screenWidth / 2,
                centerY,
                Paint().apply {
                    color = Color.Black.copy(alpha = alpha).toArgb()
                    textSize = size
                    textAlign = Paint.Align.CENTER
                    style = Paint.Style.FILL_AND_STROKE
                    strokeWidth = size / 12
                }
            )
        }
    }
    if (gameStatus == GameStatus.Onboard) {
        drawText("Tetrominot".uppercase(), 60f)
    } else if (gameStatus == GameStatus.GameOver) {
        drawText("GAME OVER", 60f)
    }
}

private fun DrawScope.drawMatrix(
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f,
) {
    (0 until matrix.first).forEach { x ->
        (0 until matrix.second).forEach { y ->
            drawBrick(
                brickSize,
                Offset(
                    x.toFloat() + leftOffset,
                    y.toFloat(),
                ),
                BrickMatrix
            )
        }
    }
}

private fun DrawScope.drawMatrixBorder(
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float
) {

    val gap = matrix.first * brickSize * 0.05f
    drawRect(
        Color.Black,
        size = Size(matrix.first * brickSize + gap, matrix.second * brickSize + gap),
        topLeft = Offset(leftOffset - gap / 2, -gap / 2),
        style = Stroke(0.8.dp.toPx())
    )
}

private fun DrawScope.drawBricks(
    brick: List<Brick>,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f
) {
    clipRect(0f, 0f, bottom = matrix.second * brickSize) {
        brick.forEach {
            val (x, y) = it.location

            drawBrick(
                brickSize,
                Offset(
                    x + leftOffset,
                    y,
                ),
                BrickSpirit
            )
        }
    }
}

private fun DrawScope.drawSpirit(
    spirit: Spirit,
    brickSize: Float,
    matrix: Pair<Int, Int>,
    leftOffset: Float = 0f
) {
    clipRect(0f, 0f, bottom = matrix.second * brickSize) {
        spirit.location.forEach {
            drawBrick(
                brickSize,
                Offset(
                    it.x + leftOffset,
                    it.y,
                ),
                BrickSpirit
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

    val innerSize = brickSize * 0.5f
    val innerOffset = (brickSize - innerSize) / 2

    drawRect(
        color,
        actualLocation + Offset(innerOffset, innerOffset),
        size = Size(innerSize, innerSize)
    )
}

@Composable
fun PreviewGameScreen(modifier: Modifier = Modifier) {
    GameScreen(modifier)
}

@Preview
@Composable
fun PreviewSpiritType() {
    Row(Modifier.size(300.dp, 50.dp).background(ScreenBackground)) {
        val matrix = 2 to 4
        SpiritType.forEach {
            Canvas(Modifier.weight(1f).fillMaxHeight().padding(5.dp)) {
                drawBricks(
                    Brick.of(Spirit(it).adjustOffset(matrix)),
                    min(size.width / matrix.first, size.height / matrix.second),
                    matrix
                )
            }
        }
    }
}
