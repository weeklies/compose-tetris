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
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jetgame.tetris.logic.*
import com.jetgame.tetris.ui.GameBackground
import com.jetgame.tetris.ui.GameScreen
import com.jetgame.tetris.ui.PreviewGameScreen
import com.jetgame.tetris.ui.SettingsScreen
import com.jetgame.tetris.ui.theme.TetrominautsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getPreferences(MODE_PRIVATE)
        SoundUtil.init(this)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "game",
            ) {
                composable("game") {
                    val viewModel = viewModel<GameViewModel>(factory = GameViewModelFactory(prefs))
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

                    TetrominautsTheme(viewState.isDarkMode) {
                        Scaffold { padding ->
                            GameBackground(Modifier.padding(padding)) { modifier ->
                                GameScreen(
                                    modifier,
                                    interactive =
                                        combinedInteractive(
                                            onMove = { direction: Direction ->
                                                if (direction == Direction.Up)
                                                    viewModel.dispatch(Action.Drop)
                                                else viewModel.dispatch(Action.Move(direction))
                                            },
                                            onRotate = { viewModel.dispatch(Action.Rotate) },
                                            onMute = { viewModel.dispatch(Action.Mute) },
                                            onDarkMode = { viewModel.dispatch(Action.DarkMode) },
                                            onPause = {
                                                if (viewModel.viewState.value.isRunning) {
                                                    viewModel.dispatch(Action.Pause)
                                                } else {
                                                    viewModel.dispatch(Action.Resume)
                                                }
                                            },
                                            onRestart = { viewModel.dispatch(Action.Reset) },
                                            onInfo = {
                                                viewModel.dispatch(Action.ToggleInfoDialog)
                                            },
                                            onSettings = {
                                                navController.navigate(
                                                    "settings/${viewState.isDarkMode}"
                                                )
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
                composable(
                    "settings/{isDark}",
                    arguments = listOf(navArgument("isDark") { type = NavType.BoolType })
                ) { stack ->
                    val isDark = stack.arguments?.getBoolean("isDark") ?: true

                    TetrominautsTheme(isDark) {
                        Scaffold { padding ->
                            GameBackground(Modifier.padding(padding)) { modifier ->
                                SettingsScreen(
                                    modifier,
                                    GameSettings(
                                        useNauts = {
                                            prefs.edit().putBoolean(useNauts, it).apply()
                                        },
                                        useGhostBlock = {
                                            prefs.edit().putBoolean(useGhostBlock, it).apply()
                                        },
                                        showGridOutline = {
                                            prefs.edit().putBoolean(showGridOutline, it).apply()
                                        },
                                        setNautProbability = {
                                            prefs.edit().putFloat(nautsProbability, it).apply()
                                        },
                                        setGameSpeed = {
                                            prefs.edit().putFloat(gameSpeed, it).apply()
                                        },
                                        setMatrixHeight = {
                                            val h = it.toInt()
                                            prefs.edit().putInt(gridHeight, h).apply()
                                        },
                                        setMatrixWidth = {
                                            val w = it.toInt()
                                            prefs.edit().putInt(gridWidth, w).apply()
                                        },
                                        navigateBack = { navController.popBackStack() },
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        SoundUtil.pause()
    }

    override fun onResume() {
        super.onResume()
        val prefs = getPreferences(MODE_PRIVATE)
        SoundUtil.resume(prefs.getBoolean(isMute, false))
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundUtil.release()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TetrominautsTheme { GameBackground { PreviewGameScreen(Modifier.fillMaxSize()) } }
}
