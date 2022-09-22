package com.jetgame.tetris.logic

import androidx.compose.ui.geometry.Offset

// The tetromino that has been set.
//
// Design-wise, a Block is made up of Bricks.
// Under the hood it is drawn through calling drawBricks repeatedly.
data class Block(
    val location: Offset = Offset(0, 0),
    val colorIndex: Int,
) {
    companion object {
        fun of(dropBlock: DropBlock) = of(dropBlock.location, dropBlock.colorIndex)

        // Used to generate screenClearing effect. Default color is Gray.
        fun of(xRange: IntRange, yRange: IntRange, colorIndex: Int = 0): List<Block> {
            return of(
                mutableListOf<Offset>().apply {
                    xRange.forEach { x -> yRange.forEach { y -> this += Offset(x, y) } }
                },
                colorIndex,
            )
        }

        private fun of(offsetList: List<Offset>, colorIndex: Int): List<Block> {
            return offsetList.map { Block(it, colorIndex) }
        }
    }

    fun offsetBy(step: Pair<Int, Int>) =
        copy(location = Offset(location.x + step.first, location.y + step.second))
}
