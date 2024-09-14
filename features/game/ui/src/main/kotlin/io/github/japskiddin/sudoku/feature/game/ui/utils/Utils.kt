package io.github.japskiddin.sudoku.feature.game.ui.utils

import io.github.japskiddin.sudoku.core.game.utils.BoardList
import io.github.japskiddin.sudoku.core.game.utils.convertToList
import io.github.japskiddin.sudoku.core.model.GameType

internal fun findGameTypeBySize(size: Int): GameType = when (size) {
    GameType.DEFAULT6X6.size -> GameType.DEFAULT6X6
    GameType.DEFAULT9X9.size -> GameType.DEFAULT9X9
    GameType.DEFAULT12X12.size -> GameType.DEFAULT12X12
    GameType.UNSPECIFIED.size -> GameType.UNSPECIFIED
    else -> throw IllegalArgumentException("Incorrect game type size")
}

internal fun getSampleBoardForPreview(): BoardList {
    val board = "760000009040500800090006364500040041904070000836900000000080900000006007407000580"
    return board.convertToList(GameType.DEFAULT9X9)
}
