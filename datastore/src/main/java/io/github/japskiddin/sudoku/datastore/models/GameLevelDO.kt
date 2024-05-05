package io.github.japskiddin.sudoku.datastore.models

data class GameLevelDO(
    val playtime: Long,
    val board: String,
    val completedBoard: String,
    val actions: Int,
    val difficulty: Int,
)