package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.game.utils.BoardList
import io.github.japskiddin.sudoku.core.game.utils.emptyBoardList
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType

internal data class GameState(
    val initialBoard: BoardList,
    val solvedBoard: BoardList,
    val board: BoardList,
    val notes: List<BoardNote>,
    val selectedCell: BoardCell,
    val type: GameType,
    val difficulty: GameDifficulty,
    val actions: Int,
    val mistakes: Int,
    val time: Long,
    val status: Status,
    val error: GameError
) {
    internal enum class Status {
        LOADING,
        PLAYING,
        COMPLETED
    }

    internal companion object {
        val Initial: GameState = GameState(
            board = emptyBoardList,
            initialBoard = listOf(),
            solvedBoard = listOf(),
            notes = listOf(),
            selectedCell = BoardCell.Empty,
            type = GameType.DEFAULT9X9,
            difficulty = GameDifficulty.INTERMEDIATE,
            actions = 0,
            mistakes = 0,
            time = 0L,
            status = Status.LOADING,
            error = GameError.NONE
        )
    }
}
