package com.jetgame.tetris.ui

import android.view.LayoutInflater
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jetgame.tetris.R
import com.jetgame.tetris.ui.stars.AnimatedStarsView

@Composable
fun GameBody(modifier: Modifier = Modifier, screen: @Composable () -> Unit) {
    // Game Display
    Box(modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 40.dp)) {
        AndroidView(
            factory = {
                val view =
                    LayoutInflater.from(it)
                        .inflate(
                            R.layout.animated_stars,
                            null,
                            false,
                        )

                val stars = view.findViewById<AnimatedStarsView>(R.id.stars)
                val stars_white = view.findViewById<AnimatedStarsView>(R.id.stars_white)

                stars_white.onStart()
                stars.onStart()
                view
            }
        )
        screen()
    }
}

@Preview(widthDp = 400, heightDp = 700, showBackground = true)
@Composable
fun PreviewGameBody() {
    GameBody {}
}
