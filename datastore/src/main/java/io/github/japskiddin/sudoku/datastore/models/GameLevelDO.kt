package io.github.japskiddin.sudoku.datastore.models

data class GameLevelDO(
    val time: Long,
    val board: String,
    val completedBoard: String,
    val actions: Int,
    val difficulty: Int,
)