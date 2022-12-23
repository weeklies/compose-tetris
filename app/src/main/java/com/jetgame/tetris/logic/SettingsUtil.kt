package com.jetgame.tetris.logic

data class GameSettings
constructor(
    val useNauts: (Boolean) -> Unit,
    val useGhostBlock: (Boolean) -> Unit,
    val showGridOutline: (Boolean) -> Unit,
    val setNautProbability: (Float) -> Unit,
    val setGameSpeed: (Float) -> Unit,
    val setMatrixHeight: (Float) -> Unit,
    val setMatrixWidth: (Float) -> Unit,
    val navigateBack: () -> Unit,
)

const val isMute = "isMute"
const val isDarkMode = "isDarkMode"

const val useNauts = "useNauts"
const val nautsProbability = "nautsProbability"
const val useGhostBlock = "useGhostBlock"
const val showGridOutline = "showGridOutline"
const val gameSpeed = "gameSpeed"
const val gridHeight = "gridHeight"
const val gridWidth = "gridWidth"
