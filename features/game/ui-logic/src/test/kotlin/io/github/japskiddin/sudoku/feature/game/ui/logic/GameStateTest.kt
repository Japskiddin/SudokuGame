package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import org.junit.Assert.assertEquals
import org.junit.Test

class GameStateTest {
    @Test
    fun toGameHistory_capturesBoardNotesAndCounters() {
        val state = gameState(
            boardValue = 4,
            actions = 6,
            mistakes = 2,
            notes = listOf(BoardNote(0, 0, 4)),
        )

        val history = state.toGameHistory()

        assertEquals(state.board, history.board)
        assertEquals(state.notes, history.notes)
        assertEquals(state.actions, history.actions)
        assertEquals(state.mistakes, history.mistakes)
    }

    @Test
    fun restoreFromHistory_replacesBoardNotesAndCounters() {
        val initial = gameState(
            boardValue = 1,
            actions = 1,
            mistakes = 0,
            notes = emptyList(),
        )
        val history = GameHistory(
            board = boardWithValue(9),
            notes = listOf(BoardNote(0, 0, 9)),
            actions = 5,
            mistakes = 3,
        )

        val restored = initial.restoreFromHistory(history)

        assertEquals(history.board, restored.board)
        assertEquals(history.notes, restored.notes)
        assertEquals(history.actions, restored.actions)
        assertEquals(history.mistakes, restored.mistakes)
    }

    @Test
    fun resetProgress_clearsCountersNotesAndSelection() {
        val state = gameState(
            boardValue = 8,
            initialBoardValue = 2,
            actions = 7,
            mistakes = 4,
            time = 12L,
            notes = listOf(BoardNote(0, 0, 8)),
            selectedCell = BoardCell(0, 0, 8, isError = true),
        )

        val reset = state.resetProgress(isResetTimer = true)

        assertEquals(state.initialBoard, reset.board)
        assertEquals(0, reset.actions)
        assertEquals(0, reset.mistakes)
        assertEquals(emptyList<BoardNote>(), reset.notes)
        assertEquals(0L, reset.time)
        assertEquals(BoardCell.Empty, reset.selectedCell)
    }

    @Test
    fun resetProgress_keepsElapsedTimeWhenConfigured() {
        val state = gameState(time = 25L)

        val reset = state.resetProgress(isResetTimer = false)

        assertEquals(25L, reset.time)
    }

    private fun gameState(
        boardValue: Int = 1,
        initialBoardValue: Int = boardValue,
        actions: Int = 0,
        mistakes: Int = 0,
        time: Long = 0L,
        notes: List<BoardNote> = emptyList(),
        selectedCell: BoardCell = BoardCell.Empty,
    ): GameState = GameState(
        initialBoard = boardWithValue(initialBoardValue),
        solvedBoard = boardWithValue(9),
        board = boardWithValue(boardValue),
        notes = notes,
        selectedCell = selectedCell,
        type = GameType.DEFAULT9X9,
        difficulty = GameDifficulty.INTERMEDIATE,
        actions = actions,
        mistakes = mistakes,
        time = time,
        status = GameState.Status.PLAYING,
        error = GameError.NONE,
    )

    private fun boardWithValue(value: Int): List<List<BoardCell>> = listOf(
        listOf(BoardCell(row = 0, col = 0, value = value))
    )
}
