package com.jetgame.tetris.ui

import android.view.LayoutInflater
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jetgame.tetris.R
import com.jetgame.tetris.ui.stars.AnimatedStarsView

@Composable
fun GameBackground(modifier: Modifier = Modifier, screen: @Composable (Modifier) -> Unit) {
    // Game Display
    Box(modifier) {
        AndroidView(
            factory = {
                val view = LayoutInflater.from(it).inflate(R.layout.animated_stars, null, false)

                val stars_white = view.findViewById<AnimatedStarsView>(R.id.stars_white)
                stars_white.onStart()
                view
            }
        )

        screen(Modifier.padding(horizontal = 16.dp, vertical = 40.dp))
    }
}

@Preview(widthDp = 400, heightDp = 700, showBackground = true)
@Composable
fun PreviewGameBody() {
    GameBackground {}
}
