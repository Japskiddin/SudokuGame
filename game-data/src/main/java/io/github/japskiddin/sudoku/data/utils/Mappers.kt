package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.data.models.Difficulty
import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.datastore.models.GameLevelDO

internal const val BOARD_SEPARATOR = ""

internal fun GameLevel.toGameLevelDO(): GameLevelDO {
    return GameLevelDO(
        this.time,
        this.board.toBoardString(),
        this.actions,
        this.difficulty.toInt()
    )
}

internal fun GameLevelDO.toGameLevel(): GameLevel {
    return GameLevel(
        this.time,
        this.board.toIntArray(),
        this.actions,
        this.difficulty.toDifficulty(),
    )
}

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

internal fun String.toIntArray(): IntArray {
    val items = split(BOARD_SEPARATOR)
    val array = IntArray(items.size)
    items.forEachIndexed { i, item ->
        val number = try {
            item.toInt()
        } catch (ex: NumberFormatException) {
            -1
        }
        array[i] = number
    }
    return array
}

internal fun IntArray.toBoardString(): String {
    val sb = StringBuilder()
    forEachIndexed { i, item ->
        sb.append(item.toString())
        if (i + 1 < size) {
            sb.append(BOARD_SEPARATOR)
        }
    }
    return sb.toString()
}