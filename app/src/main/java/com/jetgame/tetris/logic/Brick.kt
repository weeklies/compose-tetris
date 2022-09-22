package com.jetgame.tetris.logic

import androidx.compose.ui.geometry.Offset

// The tetromino that has been set.
data class Brick(
    val location: Offset = Offset(0, 0),
    val colorIndex: Int,
) {
    companion object {
        fun of(spirit: Spirit) = of(spirit.location, spirit.colorIndex)

        // Used to generate screenClearing effect. Default color is Gray.
        fun of(xRange: IntRange, yRange: IntRange, colorIndex: Int = 0): List<Brick> {
            return of(
                mutableListOf<Offset>().apply {
                    xRange.forEach { x -> yRange.forEach { y -> this += Offset(x, y) } }
                },
                colorIndex,
            )
        }

        private fun of(offsetList: List<Offset>, colorIndex: Int): List<Brick> {
            return offsetList.map { Brick(it, colorIndex) }
        }
    }

    fun offsetBy(step: Pair<Int, Int>) =
        copy(location = Offset(location.x + step.first, location.y + step.second))
}
