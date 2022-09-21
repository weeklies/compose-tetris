package com.jetgame.tetris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetgame.tetris.logic.*
import com.jetgame.tetris.ui.GameBody
import com.jetgame.tetris.ui.GameScreen
import com.jetgame.tetris.ui.PreviewGameScreen
import com.jetgame.tetris.ui.theme.ComposetetrisTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        StatusBarUtil.transparentStatusBar(this)
        SoundUtil.init(this)

        setContent {
            ComposetetrisTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = viewModel<GameViewModel>()
                val viewState = viewModel.viewState.value

                LaunchedEffect(key1 = Unit) {
                    while (isActive) {
                        delay(650L - 55 * (viewState.level - 1))
                        viewModel.dispatch(Action.GameTick)
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = Unit) {
                    val observer =
                        object : DefaultLifecycleObserver {
                            override fun onResume(owner: LifecycleOwner) {
                                viewModel.dispatch(Action.Resume)
                            }

                            override fun onPause(owner: LifecycleOwner) {
                                viewModel.dispatch(Action.Pause)
                            }
                        }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                Scaffold { padding ->
                    GameBody(Modifier.padding(padding)) {
                        GameScreen(
                            interactive =
                                combinedInteractive(
                                    onMove = { direction: Direction ->
                                        if (direction == Direction.Up)
                                            viewModel.dispatch(Action.Drop)
                                        else viewModel.dispatch(Action.Move(direction))
                                    },
                                    onRotate = { viewModel.dispatch(Action.Rotate) },
                                    onMute = { viewModel.dispatch(Action.Mute) },
                                    onPause = {
                                        if (viewModel.viewState.value.isRunning) {
                                            viewModel.dispatch(Action.Pause)
                                        } else {
                                            viewModel.dispatch(Action.Resume)
                                        }
                                    },
                                    onRestart = { viewModel.dispatch(Action.Reset) },
                                )
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundUtil.release()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposetetrisTheme { GameBody { PreviewGameScreen(Modifier.fillMaxSize()) } }
}
