package io.github.japskiddin.sudoku.core.model

public data class GameMode(
    val difficulty: GameDifficulty,
    val type: GameType,
) {
    public companion object {
        public val Initial: GameMode = GameMode(
            difficulty = GameDifficulty.EASY,
            type = GameType.DEFAULT9X9
        )
    }
}
