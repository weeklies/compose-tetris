package com.jetgame.tetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetgame.tetris.R
import com.jetgame.tetris.logic.Direction
import com.jetgame.tetris.ui.theme.BodyColor
import com.jetgame.tetris.ui.theme.ScreenBackground
import kotlin.math.absoluteValue

@Composable
fun GameBody(clickable: Clickable = combinedClickable(), screen: @Composable () -> Unit) {

    Column(
        Modifier.fillMaxSize()
            .background(Color.Black)
            .background(BodyColor, RoundedCornerShape(10.dp))
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        // Screen
        Column {
            // Setting Button
            Column(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
                val settingText =
                    @Composable { text: String, modifier: Modifier ->
                        Text(
                            text,
                            modifier = modifier,
                            color = Color.Black.copy(0.9f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                Text(
                    stringResource(id = R.string.body_label),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    settingText(
                        stringResource(id = R.string.button_sounds),
                        Modifier.weight(1f),
                    )
                    settingText(
                        stringResource(id = R.string.button_pause),
                        Modifier.weight(1f),
                    )
                    settingText(
                        stringResource(id = R.string.button_reset),
                        Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row {

                    // SOUNDS
                    GameButton(
                        modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                        onClick = { clickable.onMute() },
                        size = SettingButtonSize
                    ) {}

                    // PAUSE
                    GameButton(
                        modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                        onClick = { clickable.onPause() },
                        size = SettingButtonSize
                    ) {}

                    // RESET
                    GameButton(
                        modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                        onClick = { clickable.onRestart() },
                        size = SettingButtonSize
                    ) {}
                }
            }

            var swipeDirection = SwipeDirection.None

            // Game Display
            Box(
                Modifier.size(500.dp, 400.dp)
                    .padding(start = 50.dp, end = 50.dp, top = 30.dp, bottom = 30.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consumeAllChanges()

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
                                } else if (absX < absY) {
                                    when {
                                        y > 0 -> swipeDirection = SwipeDirection.Down
                                        y < 0 -> swipeDirection = SwipeDirection.Up
                                    }
                                }
                            },
                            onDragEnd = {
                                when (swipeDirection) {
                                    SwipeDirection.Right -> clickable.onMove(Direction.Right)
                                    SwipeDirection.Left -> clickable.onMove(Direction.Left)
                                    SwipeDirection.Down -> clickable.onMove(Direction.Up)
                                    SwipeDirection.Up -> clickable.onRotate()
                                    SwipeDirection.None -> {}
                                }
                                swipeDirection = SwipeDirection.None
                            },
                        )
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawScreenBorder(
                        Offset(0f, 0f),
                        Offset(size.width, 0f),
                        Offset(0f, size.height),
                        Offset(size.width, size.height)
                    )
                }

                Box(modifier = Modifier.fillMaxSize().padding(6.dp).background(ScreenBackground)) {
                    screen()
                }
            }
        }

        // Game Button
        val buttonText =
            @Composable { modifier: Modifier, text: String ->
                Text(text, modifier = modifier, color = Color.White.copy(0.9f), fontSize = 18.sp)
            }

        Row(modifier = Modifier.padding(start = 40.dp, end = 40.dp).height(160.dp)) {
            // DIRECTION BTN
            Box(modifier = Modifier.fillMaxHeight().weight(1f)) {
                GameButton(
                    Modifier.align(Alignment.TopCenter),
                    onClick = { clickable.onMove(Direction.Up) },
                    autoInvokeWhenPressed = false,
                    size = DirectionButtonSize
                ) {
                    buttonText(it, stringResource(id = R.string.button_up))
                }
                GameButton(
                    Modifier.align(Alignment.CenterStart),
                    onClick = { clickable.onMove(Direction.Left) },
                    autoInvokeWhenPressed = true,
                    size = DirectionButtonSize
                ) {
                    buttonText(it, stringResource(id = R.string.button_left))
                }
                GameButton(
                    Modifier.align(Alignment.CenterEnd),
                    onClick = { clickable.onMove(Direction.Right) },
                    autoInvokeWhenPressed = true,
                    size = DirectionButtonSize
                ) {
                    buttonText(it, stringResource(id = R.string.button_right))
                }
                GameButton(
                    Modifier.align(Alignment.BottomCenter),
                    onClick = { clickable.onMove(Direction.Down) },
                    autoInvokeWhenPressed = true,
                    size = DirectionButtonSize
                ) {
                    buttonText(it, stringResource(id = R.string.button_down))
                }
            }

            // ROTATE BTN
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                GameButton(
                    Modifier.align(Alignment.CenterEnd),
                    onClick = { clickable.onRotate() },
                    autoInvokeWhenPressed = false,
                    size = RotateButtonSize
                ) {
                    buttonText(it, stringResource(id = R.string.button_rotate))
                }
            }
        }
    }
}

fun DrawScope.drawScreenBorder(
    topLef: Offset,
    topRight: Offset,
    bottomLeft: Offset,
    bottomRight: Offset
) {
    var path =
        Path().apply {
            moveTo(topLef.x, topLef.y)
            lineTo(topRight.x, topRight.y)
            lineTo(topRight.x / 2 + topLef.x / 2, topLef.y + topRight.x / 2 + topLef.x / 2)
            lineTo(topRight.x / 2 + topLef.x / 2, bottomLeft.y - topRight.x / 2 + topLef.x / 2)
            lineTo(bottomLeft.x, bottomLeft.y)
            close()
        }
    drawPath(path, Color.Black.copy(0.5f))

    path =
        Path().apply {
            moveTo(bottomRight.x, bottomRight.y)
            lineTo(bottomLeft.x, bottomLeft.y)
            lineTo(topRight.x / 2 + topLef.x / 2, bottomLeft.y - topRight.x / 2 + topLef.x / 2)
            lineTo(topRight.x / 2 + topLef.x / 2, topLef.y + topRight.x / 2 + topLef.x / 2)
            lineTo(topRight.x, topRight.y)
            close()
        }

    drawPath(path, Color.White.copy(0.5f))
}

private enum class SwipeDirection {
    Left,
    Right,
    Up,
    Down,
    None,
}

data class Clickable
constructor(
    val onMove: (Direction) -> Unit,
    val onRotate: () -> Unit,
    val onRestart: () -> Unit,
    val onPause: () -> Unit,
    val onMute: () -> Unit
)

fun combinedClickable(
    onMove: (Direction) -> Unit = {},
    onRotate: () -> Unit = {},
    onRestart: () -> Unit = {},
    onPause: () -> Unit = {},
    onMute: () -> Unit = {}
) = Clickable(onMove, onRotate, onRestart, onPause, onMute)

@Preview(widthDp = 400, heightDp = 700)
@Composable
fun PreviewGameBody() {
    GameBody {}
}

val DirectionButtonSize = 60.dp
val RotateButtonSize = 90.dp
val SettingButtonSize = 15.dp
