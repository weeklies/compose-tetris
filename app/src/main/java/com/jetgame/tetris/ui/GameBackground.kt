package com.jetgame.tetris.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetgame.tetris.R
import com.jetgame.tetris.databinding.AnimatedStarsBinding
import com.jetgame.tetris.logic.GameViewModel
import kotlin.random.Random

@Composable
fun GameBackground(
    modifier: Modifier = Modifier,
    viewState: GameViewModel.ViewState,
    screen: @Composable (Modifier) -> Unit
) {
    // Game Display
    Box(modifier.clearAndSetSemantics { disabled() }) {
        AnimatedStars(viewState.isDarkMode)
        if (viewState.showBackgroundArt) {
            AnimatedPlanet1(viewState.isDarkMode)
            AnimatedPlanet2(viewState.isDarkMode)
            if (viewState.isDarkMode) AnimatedNebula() else AnimatedPlanet3()
            AnimatedShipsAndAsteroids(viewState.isDarkMode)
        }
        screen(Modifier.padding(horizontal = 20.dp, vertical = 8.dp).padding(bottom = 26.dp))
    }
}

@Composable
fun AnimatedStars(darkMode: Boolean) {
    if (darkMode) AndroidViewBinding(AnimatedStarsBinding::inflate) { this.starsDarkMode.onStart() }
    else AndroidViewBinding(AnimatedStarsBinding::inflate) { this.starsLightMode.onStart() }
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

    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(6f)) {
            Image(
                painter = painterResource(R.drawable.nebula),
                contentDescription = "",
                alpha = 0.50f,
                modifier =
                    Modifier.offset((-50).dp, 60.dp).fillMaxSize().graphicsLayer {
                        rotationZ = angle
                    },
                alignment = Alignment.BottomStart,
            )
        }
        Spacer(Modifier.weight(2f))
    }
}

@Composable
fun AnimatedPlanet3() {
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

    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            Image(
                painter = painterResource(planets3.random()),
                contentDescription = "",
                alpha = 0.9f,
                modifier =
                    Modifier.offset((-50).dp, 40.dp).fillMaxSize().graphicsLayer {
                        rotationZ = angle
                    },
                alignment = Alignment.BottomStart,
            )
        }
        Spacer(Modifier.weight(2f))
    }
}

// The planet on the center right.
@Composable
fun AnimatedPlanet1(darkMode: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by
        infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            200 /* seconds */ * 1000,
                            easing = LinearEasing,
                        ),
                    repeatMode = RepeatMode.Restart,
                )
        )

    Row(Modifier.fillMaxSize()) {
        Spacer(Modifier.weight(3f))
        Box(Modifier.weight(1f)) {
            Image(
                painter = painterResource(planets1.random()),
                contentDescription = "",
                alpha = if (darkMode) 0.50f else 0.7f,
                modifier =
                    Modifier.offset(40.dp, 0.dp).fillMaxSize().graphicsLayer { rotationZ = angle },
            )
        }
    }
}

// The planet on the top left.
@Composable
fun AnimatedPlanet2(darkMode: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by
        infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            180 /* seconds */ * 1000,
                            easing = LinearEasing,
                        ),
                    repeatMode = RepeatMode.Restart,
                )
        )

    Row(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            Image(
                painter = painterResource(planets2.random()),
                contentDescription = "",
                alpha = if (darkMode) 0.60f else 0.8f,
                modifier = Modifier.offset((-35).dp, 80.dp).graphicsLayer { rotationZ = angle },
            )
        }
        Spacer(Modifier.weight(3f))
    }
}

@Composable
fun AnimatedShipsAndAsteroids(darkMode: Boolean) {
    val count = Random.nextInt(3, 5)
    val ships = shipsAndAsteroids.shuffled().take(count)
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(count) {
            Ship(
                horizontalPadding = 24,
                verticalPadding = 120,
                imageBitmap = ImageBitmap.imageResource(ships[it]),
                opacity = if (darkMode) 0.7f else 0.8f,
            )
        }
    }
}

@Composable
fun Ship(
    horizontalPadding: Int,
    imageBitmap: ImageBitmap,
    verticalPadding: Int,
    opacity: Float,
) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

    val speedRandom = remember { Random.nextInt(20 * 1000, 60 * 1000) }
    val rotationDegrees = remember { Random.nextInt(0, 360).toFloat() }

    // Extra top padding for scoreboard
    val extraTopPadding = 20
    val yRandom = remember {
        Random.nextInt(verticalPadding + extraTopPadding, (height - verticalPadding))
    }
    val yRandom2 = remember {
        Random.nextInt(verticalPadding + extraTopPadding, (height - verticalPadding))
    }
    val xRandom = remember { Random.nextInt(horizontalPadding, (width - horizontalPadding)) }
    val xRandom2 = remember { Random.nextInt(horizontalPadding, (width - horizontalPadding)) }

    val infiniteTransition = rememberInfiniteTransition()
    val offsetX by
        infiniteTransition.animateValue(
            initialValue = xRandom2.dp,
            targetValue = xRandom.dp,
            typeConverter = Dp.VectorConverter,
            animationSpec =
                infiniteRepeatable(
                    animation =
                        tween(
                            speedRandom,
                            easing = FastOutLinearInEasing,
                        ),
                    repeatMode = RepeatMode.Reverse,
                )
        )
    val offsetY by
        infiniteTransition.animateValue(
            initialValue = yRandom2.dp,
            targetValue = yRandom.dp,
            typeConverter = Dp.VectorConverter,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(speedRandom),
                    repeatMode = RepeatMode.Reverse,
                )
        )

    Canvas(
        modifier = Modifier.offset(y = offsetY, x = offsetX).rotate(degrees = rotationDegrees),
        onDraw = { drawImage(image = imageBitmap, alpha = opacity) }
    )
}

private val shipsAndAsteroids =
    listOf(
        R.drawable.asteroid1,
        R.drawable.asteroid2,
        R.drawable.ship_blue_stroked,
        R.drawable.ship_green_stroked,
        R.drawable.ship_red_stroked,
        R.drawable.ship_purple_stroked,
        R.drawable.ship_orange_stroked,
        R.drawable.ship_yellow_stroked,
    )

private val planets1 =
    listOf(
        R.drawable.aplanet1,
        R.drawable.aplanet2,
        R.drawable.aplanet3,
        R.drawable.aplanet4,
    )
private val planets2 =
    listOf(
        R.drawable.planet1,
        R.drawable.planet2,
        R.drawable.planet3,
        R.drawable.planet4,
    )
private val planets3 =
    listOf(
        R.drawable.bplanet1,
        R.drawable.bplanet2,
        R.drawable.bplanet3,
        R.drawable.bplanet4,
    )
