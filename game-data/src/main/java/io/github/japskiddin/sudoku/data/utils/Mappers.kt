package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.data.models.Difficulty

internal const val BOARD_SEPARATOR = ";"

internal fun Int.toDifficulty(): Difficulty {
  return when (this) {
    1 -> Difficulty.EASY
    2 -> Difficulty.NORMAL
    3 -> Difficulty.HARD
    4 -> Difficulty.EXPERT
    else -> Difficulty.NORMAL
  }
}

internal fun Difficulty.toInt(): Int {
  return when (this) {
    Difficulty.EASY -> 1
    Difficulty.NORMAL -> 2
    Difficulty.HARD -> 3
    Difficulty.EXPERT -> 4
  }
}

internal fun String.toIntArray(): Array<IntArray> {
  val items = split(BOARD_SEPARATOR)
  val size = items.size
  val array = Array(size) { IntArray(size) }
  items.forEachIndexed { i, item ->
    array[i] = item
      .removeSurrounding("[", "]")
      .split(",")
      .map { it.toInt() }
      .toIntArray()
  }
  return array
}

internal fun Array<IntArray>.toBoardString(): String {
  val sb = StringBuilder()
  for (i in indices) {
    sb.append(this[i].contentToString())
    if (i + 1 < size) {
      sb.append(BOARD_SEPARATOR)
    }
  }
  return sb.toString()
}