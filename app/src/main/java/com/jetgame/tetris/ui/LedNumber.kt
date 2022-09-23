package com.jetgame.tetris.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetgame.tetris.logic.LedFontFamily
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@Composable
fun LedClock(modifier: Modifier = Modifier) {

    val animateValue by
        rememberInfiniteTransition()
            .animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec =
                    infiniteRepeatable(
                        animation = tween(durationMillis = 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
            )

    var clock by remember { mutableStateOf(0 to 0) }

    DisposableEffect(key1 = animateValue.roundToInt()) {
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat = SimpleDateFormat("H,m")
        val (curHou, curMin) = dateFormat.format(Date()).split(",")
        clock = curHou.toInt() to curMin.toInt()
        onDispose {}
    }

    Row(modifier) {
        LedNumber(num = clock.first, digits = 2, fillZero = true)

        val LedComma: @Composable (color: Color) -> Unit = remember {
            {
                Text(
                    ":",
                    fontFamily = LedFontFamily,
                    textAlign = TextAlign.End,
                    color = it,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier.width(6.dp).padding(end = 1.dp),
        ) {
            LedComma(MaterialTheme.colors.surface)
            if (animateValue.roundToInt() == 1) {
                LedComma(MaterialTheme.colors.onSurface)
            }
        }

        LedNumber(num = clock.second, digits = 2, fillZero = true)
    }
}

@Composable
fun LedNumber(modifier: Modifier = Modifier, num: Int, digits: Int, fillZero: Boolean = false) {
    val textSize = 16.sp
    val textWidth = 8.dp
    Box(modifier.padding(horizontal = 6.dp)) {
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
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
