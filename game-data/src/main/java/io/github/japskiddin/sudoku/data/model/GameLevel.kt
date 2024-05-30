package io.github.japskiddin.sudoku.data.model

public data class GameLevel(
  val playtime: Long = 0L,
  val defaultBoard: Array<IntArray> = emptyArray(),
  val currentBoard: Array<IntArray> = emptyArray(),
  val completedBoard: Array<IntArray> = emptyArray(),
  val actions: Int = 0,
  val difficulty: Difficulty = Difficulty.NORMAL,
) {
  public fun isEmptyBoard(): Boolean {
    return currentBoard.isEmpty() || completedBoard.isEmpty()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GameLevel

    if (playtime != other.playtime) return false
    if (!defaultBoard.contentEquals(other.defaultBoard)) return false
    if (!currentBoard.contentEquals(other.currentBoard)) return false
    if (!completedBoard.contentEquals(other.completedBoard)) return false
    if (actions != other.actions) return false
    if (difficulty != other.difficulty) return false

    return true
  }

  override fun hashCode(): Int {
    var result = playtime.hashCode()
    result = 31 * result + defaultBoard.contentHashCode()
    result = 31 * result + currentBoard.contentHashCode()
    result = 31 * result + completedBoard.contentHashCode()
    result = 31 * result + actions
    result = 31 * result + difficulty.hashCode()
    return result
  }
}