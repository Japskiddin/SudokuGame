package io.github.japskiddin.sudoku.game_data.models

import java.util.Date

data class History(
    val id: Long,
    val date: Date,
    val level: Level,
)

data class Level(
    val time: Long,
    val board: String,
    val actions: Int,
    val difficulty: Difficulty,
) {
    enum class Difficulty {
        EASY,
        NORMAL,
        HARD,
        EXPERT,
    }
}