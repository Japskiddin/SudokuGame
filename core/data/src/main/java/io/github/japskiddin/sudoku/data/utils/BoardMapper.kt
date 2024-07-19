package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.game.GameDifficulty
import io.github.japskiddin.sudoku.core.game.GameType
import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.database.model.BoardDBO

internal fun Board.toBoardDBO() =
    BoardDBO(
        uid = this.uid,
        initialBoard = this.initialBoard,
        solvedBoard = this.solvedBoard,
        difficulty = this.difficulty.toInt(),
        type = this.type.toInt()
    )

internal fun BoardDBO.toBoard() =
    Board(
        uid = this.uid,
        initialBoard = this.initialBoard,
        solvedBoard = this.solvedBoard,
        difficulty = this.difficulty.toGameDifficulty(),
        type = this.type.toGameType()
    )

@Suppress("MagicNumber")
internal fun GameDifficulty.toInt() =
    when (this) {
        GameDifficulty.UNSPECIFIED -> 0
        GameDifficulty.EASY -> 1
        GameDifficulty.INTERMEDIATE -> 2
        GameDifficulty.HARD -> 3
        GameDifficulty.EXPERT -> 4
        else -> throw IncorrectGameDifficultyException(
            "Incorrect game difficulty value. Value must be ${GameDifficulty.entries}, found $this"
        )
    }

@Suppress("MagicNumber")
internal fun Int.toGameDifficulty() =
    when (this) {
        0 -> GameDifficulty.UNSPECIFIED
        1 -> GameDifficulty.EASY
        2 -> GameDifficulty.INTERMEDIATE
        3 -> GameDifficulty.HARD
        4 -> GameDifficulty.EXPERT
        else -> throw IncorrectGameDifficultyException(
            "Incorrect game difficulty value. Value must be between 0-4, found $this"
        )
    }

@Suppress("MagicNumber")
internal fun GameType.toInt() =
    when (this) {
        GameType.UNSPECIFIED -> 0
        GameType.DEFAULT6X6 -> 1
        GameType.DEFAULT9X9 -> 2
        GameType.DEFAULT12X12 -> 3
        else -> throw IncorrectGameDifficultyException(
            "Incorrect game difficulty value. Value must be ${GameType.entries}, found $this"
        )
    }

@Suppress("MagicNumber")
internal fun Int.toGameType() =
    when (this) {
        0 -> GameType.UNSPECIFIED
        1 -> GameType.DEFAULT6X6
        2 -> GameType.DEFAULT9X9
        3 -> GameType.DEFAULT12X12
        else -> throw IncorrectGameDifficultyException(
            "Incorrect game difficulty value. Value must be between 0-3, found $this"
        )
    }

public class IncorrectGameDifficultyException(message: String) : Exception(message)

public class IncorrectGameTypeException(message: String) : Exception(message)
