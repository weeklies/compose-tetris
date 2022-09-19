package com.jetgame.tetris.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
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

    // Screen
    Column(
        Modifier.fillMaxSize().background(BodyColor, RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center,
    ) {
        // Setting Button
        Column(modifier = Modifier.padding(start = 40.dp, end = 40.dp)) {
            Text(
                stringResource(id = R.string.body_label),
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            val settingText =
                @Composable { text: String, modifier: Modifier ->
                    Text(
                        text,
                        modifier = modifier,
                        color = Color.Black.copy(0.9f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                    )
                }
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
                )

                // PAUSE
                GameButton(
                    modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                    onClick = { clickable.onPause() },
                    size = SettingButtonSize
                )

                // RESET
                GameButton(
                    modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                    onClick = { clickable.onRestart() },
                    size = SettingButtonSize
                )
            }
        }

        var swipeDirection = SwipeDirection.None

        // Game Display
        Box(
            Modifier.size(
                    400.dp,
                    500.dp,
                )
                .padding(top = 30.dp)
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
                            } else {
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
            Box(modifier = Modifier.fillMaxSize().padding(6.dp).background(ScreenBackground)) {
                screen()
            }
        }
    }
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

val SettingButtonSize = 15.dp
