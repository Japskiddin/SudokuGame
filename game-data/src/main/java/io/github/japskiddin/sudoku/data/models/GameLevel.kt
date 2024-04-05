package io.github.japskiddin.sudoku.data.models

data class GameLevel(
    val time: Long = 0,
    val board: Array<IntArray>,
    val actions: Int = 0,
    val difficulty: Difficulty = Difficulty.NORMAL,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameLevel

        if (time != other.time) return false
        if (!board.contentEquals(other.board)) return false
        if (actions != other.actions) return false
        if (difficulty != other.difficulty) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time.hashCode()
        result = 31 * result + board.contentHashCode()
        result = 31 * result + actions
        result = 31 * result + difficulty.hashCode()
        return result
    }
}