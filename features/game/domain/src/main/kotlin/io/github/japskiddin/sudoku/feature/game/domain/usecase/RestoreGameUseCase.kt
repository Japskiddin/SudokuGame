package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.game.utils.isValidCell
import io.github.japskiddin.sudoku.core.game.utils.isValidCellDynamic
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.MistakesMethod
import javax.inject.Inject

public class RestoreGameUseCase @Inject constructor() {
    public operator fun invoke(
        restoredBoard: BoardList,
        type: GameType,
        initialBoard: BoardList,
        solvedBoard: BoardList,
        mistakesMethod: MistakesMethod = MistakesMethod.CLASSIC
    ): BoardList {
        for (i in restoredBoard.indices) {
            for (j in restoredBoard.indices) {
                restoreCell(
                    cell = restoredBoard[i][j],
                    initialCell = initialBoard[i][j],
                    mistakesMethod = mistakesMethod,
                    type = type,
                    restoredBoard = restoredBoard,
                    solvedBoard = solvedBoard
                )
            }
        }
        return restoredBoard
    }

    private fun restoreCell(
        cell: BoardCell,
        initialCell: BoardCell,
        mistakesMethod: MistakesMethod,
        type: GameType,
        restoredBoard: BoardList,
        solvedBoard: BoardList
    ) {
        cell.isLocked = initialCell.isLocked
        if (cell.value != 0 && !cell.isLocked) {
            cell.isError = when (mistakesMethod) {
                MistakesMethod.MODERN -> !isValidCellDynamic(
                    board = restoredBoard,
                    cell = cell,
                    type = type
                )

                MistakesMethod.CLASSIC -> isValidCell(restoredBoard, solvedBoard, cell)
            }
        }
    }
}
