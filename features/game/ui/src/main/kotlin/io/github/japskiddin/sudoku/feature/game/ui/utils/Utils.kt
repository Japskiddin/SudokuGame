package io.github.japskiddin.sudoku.feature.game.ui.utils

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.ImmutableBoardList
import io.github.japskiddin.sudoku.core.model.toImmutable
import kotlin.random.Random

internal fun findGameTypeBySize(size: Int): GameType = when (size) {
    GameType.DEFAULT6X6.size -> GameType.DEFAULT6X6
    GameType.DEFAULT9X9.size -> GameType.DEFAULT9X9
    GameType.DEFAULT12X12.size -> GameType.DEFAULT12X12
    GameType.UNSPECIFIED.size -> GameType.UNSPECIFIED
    else -> throw IllegalArgumentException("Incorrect game type size: $size")
}

internal fun getSampleBoardForPreview(size: Int = 9): ImmutableBoardList = List(size) { row ->
    List(size) { col ->
        BoardCell(row, col, Random.nextInt(size))
    }
}.toImmutable()
