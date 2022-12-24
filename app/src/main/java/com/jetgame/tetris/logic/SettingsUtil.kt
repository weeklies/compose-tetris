package com.jetgame.tetris.logic

data class GameSettings
constructor(
    val useNauts: (Boolean) -> Unit,
    val useGhostBlock: (Boolean) -> Unit,
    val showGridOutline: (Boolean) -> Unit,
    val showBackgroundArt: (Boolean) -> Unit,
    val setNautProbability: (Int) -> Unit,
    val setGameSpeed: (Int) -> Unit,
    val setMatrixHeight: (Int) -> Unit,
    val setMatrixWidth: (Int) -> Unit,
    val navigateBack: () -> Unit,
)

const val isMute = "isMute"
const val isDarkMode = "isDarkMode"
const val useNauts = "useNauts"
const val nautProbability = "nautsProbability"
const val showBackgroundArt = "showBackgroundArt"
const val useGhostBlock = "useGhostBlock"
const val showGridOutline = "showGridOutline"
const val gameSpeed = "gameSpeed"
const val gridHeight = "gridHeight"
const val gridWidth = "gridWidth"
