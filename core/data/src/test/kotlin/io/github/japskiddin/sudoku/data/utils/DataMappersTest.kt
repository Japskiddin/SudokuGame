package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.common.IncorrectGameStatusException
import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import io.github.japskiddin.sudoku.database.entities.HistoryDBO
import io.github.japskiddin.sudoku.datastore.model.AppPreferencesDSO
import io.github.japskiddin.sudoku.datastore.model.GameModeDSO
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DataMappersTest {
    @Test
    fun appPreferencesMapper_copiesAllFlags() {
        val source = AppPreferencesDSO(
            isMistakesLimit = true,
            isShowTimer = false,
            isResetTimer = true,
            isHighlightErrorCells = false,
            isHighlightSimilarCells = true,
            isShowRemainingNumbers = false,
            isHighlightSelectedCell = true,
            isKeepScreenOn = false,
            isSaveGameMode = true,
        )

        assertEquals(
            AppPreferences(
                isMistakesLimit = true,
                isShowTimer = false,
                isResetTimer = true,
                isHighlightErrorCells = false,
                isHighlightSimilarCells = true,
                isShowRemainingNumbers = false,
                isHighlightSelectedCell = true,
                isKeepScreenOn = false,
                isSaveGameMode = true,
            ),
            source.toAppPreferences(),
        )
    }

    @Test
    fun gameModeMapper_roundTrips() {
        val mode = GameMode(
            difficulty = GameDifficulty.HARD,
            type = GameType.DEFAULT12X12,
        )

        assertEquals(mode, mode.toGameModeDSO().toGameMode())
        assertEquals(GameModeDSO(difficulty = 3, type = 3), mode.toGameModeDSO())
    }

    @Test
    fun boardAndHistoryMappers_roundTrip() {
        val board = Board(
            uid = 5L,
            board = "123",
            solvedBoard = "321",
            difficulty = GameDifficulty.INTERMEDIATE,
            type = GameType.DEFAULT9X9,
        )
        val history = History(uid = 5L, time = 100L)

        assertEquals(board, board.toBoardDBO().toBoard())
        assertEquals(history, history.toHistoryDBO().toHistory())
        assertEquals(
            BoardDBO(uid = 5L, board = "123", solvedBoard = "321", difficulty = 2, type = 2),
            board.toBoardDBO(),
        )
        assertEquals(HistoryDBO(uid = 5L, time = 100L), history.toHistoryDBO())
    }

    @Test
    fun savedGameMappers_roundTripAndValidateStatuses() {
        val savedGame = SavedGame(
            uid = 8L,
            board = "board",
            notes = "notes",
            actions = 2,
            mistakes = 1,
            time = 12L,
            lastPlayed = 13L,
            startedTime = 14L,
            finishedTime = 15L,
            status = GameStatus.COMPLETED,
        )

        assertEquals(savedGame, savedGame.toSavedGameDBO().toSavedGame())
        assertEquals(2, GameStatus.COMPLETED.toInt())
        assertEquals(GameStatus.FAILED, 1.toGameStatus())
        assertThrows(IncorrectGameStatusException::class.java) {
            7.toGameStatus()
        }
    }
}
