package com.jetgame.tetris.ui

import android.view.LayoutInflater
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetgame.tetris.R
import com.jetgame.tetris.logic.GameViewModel
import com.jetgame.tetris.ui.stars.AnimatedStarsView

@Composable
fun GameBackground(
    modifier: Modifier = Modifier,
    viewState: GameViewModel.ViewState,
    screen: @Composable (Modifier) -> Unit
) {
    // Game Display
    // TODO: make this light mode compatible
    if (viewState.isDarkMode)
        Box(modifier.clearAndSetSemantics { disabled() }) {
            AnimatedNebula()
            AnimatedShipsAndAsteroids()
            AndroidView(
                factory = {
                    val view = LayoutInflater.from(it).inflate(R.layout.animated_stars, null, false)

                    val stars_white = view.findViewById<AnimatedStarsView>(R.id.stars_white)
                    stars_white.onStart()
                    view
                }
            )

            screen(Modifier.padding(horizontal = 20.dp, vertical = 8.dp).padding(bottom = 26.dp))
        }
}

@Preview(widthDp = 400, heightDp = 700, showBackground = true)
@Composable
fun PreviewGameBody() {
    GameBackground(viewState = viewModel<GameViewModel>().viewState.value) {}
}

@Composable
fun AnimatedNebula() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by
        infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 60F,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            200 /* seconds */ * 1000,
                            easing = LinearEasing,
                        ),
                    repeatMode = RepeatMode.Reverse,
                )
        )

    Image(
        painter = painterResource(id = R.drawable.nebula),
        contentDescription = "nebula",
        alpha = 0.40f,
        modifier = Modifier.fillMaxHeight().graphicsLayer { rotationZ = angle },
        alignment = Alignment.BottomStart,
    )
}

@Composable
fun AnimatedShipsAndAsteroids() {
    //
}
