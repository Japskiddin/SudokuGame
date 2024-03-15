package io.github.japskiddin.sudoku.game_main

data class GameLevel(
    val time: Long,
    val board: String,
    val actions: Int,
    val difficulty: Difficulty
) {
    enum class Difficulty {
        EASY,
        NORMAL,
        HARD,
        EXPERT,
    }
}