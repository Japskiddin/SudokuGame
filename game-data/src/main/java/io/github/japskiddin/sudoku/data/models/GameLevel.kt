package io.github.japskiddin.sudoku.data.models

data class GameLevel(
    val time: Long,
    val board: String,
    val actions: Int,
    val difficulty: Difficulty
)