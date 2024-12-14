package io.github.japskiddin.sudoku.database.utils

import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.RecordDBO
import io.github.japskiddin.sudoku.database.entities.SavedGameDBO

internal fun createDummyBoard(
    uid: Long,
    board: String = "760000009040500800090006364500040041904070000836900000000080900000006007407000580",
    solvedBoard: String = "768432159143569872295817364572348691914675238836921745651784923389256417427193586",
    difficulty: Int = 2,
    type: Int = 2
): BoardDBO = BoardDBO(
    uid = uid,
    board = board,
    solvedBoard = solvedBoard,
    difficulty = difficulty,
    type = type
)

internal fun createDummySavedGame(
    uid: Long,
    board: String = "760000009040500800090006364500040041904070000836900000000080900000006007407000580",
    notes: String = "",
): SavedGameDBO = SavedGameDBO(
    uid = uid,
    board = board,
    notes = notes,
)

internal fun createDummyRecord(
    uid: Long,
    time: Long = System.currentTimeMillis(),
): RecordDBO = RecordDBO(
    uid = uid,
    time = time,
)

internal fun createDummyBoards(): List<BoardDBO> = listOf(
    createDummyBoard(uid = 1, difficulty = 2, type = 1),
    createDummyBoard(uid = 2, difficulty = 1, type = 2),
    createDummyBoard(uid = 3, difficulty = 2, type = 1),
)
