package io.github.japskiddin.sudoku.feature.game.ui.utils

import io.github.japskiddin.sudoku.core.model.GameType

internal fun findGameTypeBySize(size: Int): GameType = when (size) {
    GameType.DEFAULT6X6.size -> GameType.DEFAULT6X6
    GameType.DEFAULT9X9.size -> GameType.DEFAULT9X9
    GameType.DEFAULT12X12.size -> GameType.DEFAULT12X12
    GameType.UNSPECIFIED.size -> GameType.UNSPECIFIED
    else -> throw IllegalArgumentException("Incorrect game type size")
}
