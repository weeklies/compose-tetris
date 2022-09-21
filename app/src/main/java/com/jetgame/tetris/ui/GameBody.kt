package com.jetgame.tetris.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetgame.tetris.R
import com.jetgame.tetris.logic.Direction
import com.jetgame.tetris.ui.theme.BodyColor

@Composable
fun GameBody(interactive: Interactive = combinedInteractive(), screen: @Composable (Interactive) -> Unit) {

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
                    onClick = { interactive.onMute() },
                    size = SettingButtonSize
                )

                // PAUSE
                GameButton(
                    modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                    onClick = { interactive.onPause() },
                    size = SettingButtonSize
                )

                // RESET
                GameButton(
                    modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp),
                    onClick = { interactive.onRestart() },
                    size = SettingButtonSize
                )
            }
        }

        // Game Display
        Box(Modifier.padding(16.dp).fillMaxWidth().height(600.dp)) { screen(interactive) }
    }
}

data class Interactive
constructor(
    val onMove: (Direction) -> Unit,
    val onRotate: () -> Unit,
    val onRestart: () -> Unit,
    val onPause: () -> Unit,
    val onMute: () -> Unit
)

fun combinedInteractive(
    onMove: (Direction) -> Unit = {},
    onRotate: () -> Unit = {},
    onRestart: () -> Unit = {},
    onPause: () -> Unit = {},
    onMute: () -> Unit = {}
) = Interactive(onMove, onRotate, onRestart, onPause, onMute)

@Preview(widthDp = 400, heightDp = 700)
@Composable
fun PreviewGameBody() {
    GameBody {}
}

val SettingButtonSize = 15.dp
