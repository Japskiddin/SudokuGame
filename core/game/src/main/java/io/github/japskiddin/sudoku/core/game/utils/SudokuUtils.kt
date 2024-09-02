package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.game.GameType
import io.github.japskiddin.sudoku.core.game.model.BoardCell

public fun isValidCell(
    board: List<List<BoardCell>>,
    solvedBoard: List<List<BoardCell>>,
    cell: BoardCell
): Boolean = solvedBoard[cell.row][cell.col].value != board[cell.row][cell.col].value

public fun isValidCellDynamic(
    board: List<List<BoardCell>>,
    cell: BoardCell,
    type: GameType
): Boolean {
    for (i in getBoxRowRange(cell, type.sectionHeight)) {
        for (j in getBoxColRange(cell, type.sectionWidth)) {
            val value = board[i][j].value
            if (value != 0 && value == cell.value && (i != cell.row || j != cell.col)) {
                return false
            }
        }
    }

    for (i in 0 until type.size) {
        if ((board[i][cell.col].value == cell.value && i != cell.row) ||
            (board[cell.row][i].value == cell.value && i != cell.col)
        ) {
            return false
        }
    }
    return true
}

public fun getBoxRowRange(cell: BoardCell, sectionHeight: Int): IntRange =
    cell.row - cell.row % sectionHeight until (cell.row - cell.row % sectionHeight) + sectionHeight

public fun getBoxColRange(cell: BoardCell, sectionWidth: Int): IntRange =
    cell.col - cell.col % sectionWidth until (cell.col - cell.col % sectionWidth) + sectionWidth

