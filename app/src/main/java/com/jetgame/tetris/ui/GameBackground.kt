package com.jetgame.tetris.ui

import android.view.LayoutInflater
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetgame.tetris.R
import com.jetgame.tetris.logic.GameViewModel
import com.jetgame.tetris.ui.stars.AnimatedStarsView
import kotlin.random.Random

@Composable
fun GameBackground(
    modifier: Modifier = Modifier,
    viewState: GameViewModel.ViewState,
    screen: @Composable (Modifier) -> Unit
) {
    // Game Display
    // TODO: make this light mode compatible
    Box(modifier.clearAndSetSemantics { disabled() }) {
        if (viewState.isDarkMode) AnimatedNebula()
        if (viewState.isDarkMode) AnimatedShipsAndAsteroids()
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
        alignment = Alignment.BottomCenter,
    )
}

@Composable
fun AnimatedShipsAndAsteroids() {
    val count = Random.nextInt(2, 5)
    val ships = shipsAndAsteroids.shuffled().take(count)
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(count) {
            Ship(
                Modifier,
                horizontalPadding = 24,
                verticalPadding = 120,
                imageBitmap = ImageBitmap.imageResource(id = ships[it])
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Ship(
    modifier: Modifier,
    horizontalPadding: Int,
    imageBitmap: ImageBitmap,
    verticalPadding: Int
) {
    val width = LocalConfiguration.current.screenWidthDp
    val height = LocalConfiguration.current.screenHeightDp

    val speedRandom = Random.nextInt(20 * 1000, 240 * 1000)

    val state = remember { mutableStateOf(ShipState.Show) }
    val rotationDegrees = remember { Random.nextInt(0, 360).toFloat() }

    // Extra top padding for scoreboard
    val extraTopPadding = 20
    val yRandom = Random.nextInt(verticalPadding + extraTopPadding, (height - verticalPadding))
    val yRandom2 = Random.nextInt(verticalPadding + extraTopPadding, (height - verticalPadding))
    val xRandom = Random.nextInt(horizontalPadding, (width - horizontalPadding))
    val xRandom2 = Random.nextInt(horizontalPadding, (width - horizontalPadding))

    val offsetYAnimation: Dp by
        animateDpAsState(
            targetValue =
                when (state.value) {
                    ShipState.Show -> yRandom2.dp
                    ShipState.Hide -> yRandom.dp
                },
            animationSpec = tween(speedRandom)
        )

    val offsetXAnimation: Dp by
        animateDpAsState(
            targetValue =
                when (state.value) {
                    ShipState.Show -> xRandom2.dp
                    else -> xRandom.dp
                },
            animationSpec = tween(speedRandom)
        )

    LaunchedEffect(
        key1 = state,
        block = {
            state.value =
                when (state.value) {
                    ShipState.Show -> ShipState.Hide
                    ShipState.Hide -> ShipState.Show
                }
        }
    )

    // TODO: Check if this is efficiently working
    AnimatedContent(targetState = state.value == ShipState.Show) {
        Canvas(
            modifier =
                modifier
                    .offset(
                        y = offsetYAnimation,
                        x = offsetXAnimation,
                    )
                    .rotate(
                        degrees = rotationDegrees,
                    ),
            onDraw = {
                drawImage(
                    image = imageBitmap,
                    alpha = 0.70f,
                )
            }
        )
    }
}

private enum class ShipState {
    Show,
    Hide
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
