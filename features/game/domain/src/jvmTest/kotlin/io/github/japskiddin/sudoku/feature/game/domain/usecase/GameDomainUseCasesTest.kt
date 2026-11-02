package io.github.japskiddin.sudoku.feature.game.domain.usecase

import io.github.japskiddin.sudoku.core.common.AppDispatchers
import io.github.japskiddin.sudoku.core.common.BoardNotFoundException
import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.core.model.MistakesMethod
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.core.model.initiate
import io.github.japskiddin.sudoku.core.model.toBoardList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameDomainUseCasesTest {
    private val dispatcher = StandardTestDispatcher()
    private val appDispatchers = AppDispatchers(default = dispatcher)

    @Test
    fun checkGameStatus_prioritizesFailureThenCompletion() = runTest(dispatcher.scheduler) {
        val useCase = CheckGameStatusUseCase(appDispatchers)
        val board = boardOf(
            intArrayOf(1, 2),
            intArrayOf(3, 4),
        )

        assertEquals(
            GameStatus.FAILED,
            useCase(board, board, mistakes = GameDifficulty.EXPERT.mistakesLimit, difficulty = GameDifficulty.EXPERT),
        )
        assertEquals(
            GameStatus.COMPLETED,
            useCase(board, board, mistakes = 0, difficulty = GameDifficulty.HARD),
        )
        assertEquals(
            GameStatus.IN_PROGRESS,
            useCase(board, boardOf(intArrayOf(1, 2), intArrayOf(3, 5)), mistakes = 0, difficulty = GameDifficulty.HARD),
        )
    }

    @Test
    fun getBoard_returnsBoardOrThrows() = runTest {
        val board = Board(3L, "board", "solved", GameDifficulty.EASY, GameType.DEFAULT9X9)
        val repository = FakeBoardRepository(board)
        val useCase = GetBoardUseCase(repository)

        assertEquals(board, useCase(3L))
        try {
            useCase(77L)
            throw AssertionError("Expected BoardNotFoundException")
        } catch (error: BoardNotFoundException) {
            assertEquals("Board with 77 not found", error.message)
        }
    }

    @Test
    fun saveGame_updatesCurrentEntryOrCreatesNewOne() = runTest {
        val repository = FakeSavedGameRepository(
            current = savedGame(uid = 9L, status = GameStatus.IN_PROGRESS),
        )
        val useCase = SaveGameUseCase(repository)

        useCase(uid = 9L, board = "updated", notes = "n", actions = 3, mistakes = 2, time = 50L)

        assertEquals("updated", repository.updated?.board)
        assertEquals("n", repository.updated?.notes)

        repository.current = null
        useCase(uid = 10L, board = "new", notes = "", actions = 1, mistakes = 0, time = 5L)

        assertEquals("new", repository.inserted?.board)
        assertEquals(GameStatus.IN_PROGRESS, repository.inserted?.status)
    }

    @Test
    fun addToHistory_updatesCurrentGameAndStoresHistoryRecord() = runTest {
        val savedGameRepository = FakeSavedGameRepository(
            current = savedGame(uid = 4L, status = GameStatus.IN_PROGRESS),
        )
        val historyRepository = FakeHistoryRepository()
        val useCase = AddToHistoryUseCase(historyRepository, savedGameRepository)

        val result = useCase(
            uid = 4L,
            board = "done",
            notes = "n",
            actions = 10,
            mistakes = 1,
            time = 99L,
            status = GameStatus.COMPLETED,
        )

        assertEquals(1L, result)
        assertEquals("done", savedGameRepository.updated?.board)
        assertEquals(GameStatus.COMPLETED, savedGameRepository.updated?.status)
        assertEquals(4L, historyRepository.inserted?.uid)
    }

    @Test
    fun restoreGame_appliesLocksAndMarksErrorsForBothModes() = runTest(dispatcher.scheduler) {
        val initialBoard = board9(Triple(0, 0, 5), Triple(0, 1, 3)).also {
            it[0][0].isLocked = true
            it[0][1].isLocked = true
        }
        val restored = board9(Triple(0, 0, 5), Triple(0, 1, 3), Triple(0, 2, 4), Triple(1, 1, 5))
        val solved = board9(Triple(0, 0, 5), Triple(0, 1, 3), Triple(0, 2, 4), Triple(1, 1, 7))
        val useCase = RestoreGameUseCase(appDispatchers)

        val classic = useCase(
            restoredBoard = restored,
            type = GameType.DEFAULT9X9,
            initialBoard = initialBoard,
            solvedBoard = solved,
            mistakesMethod = MistakesMethod.CLASSIC,
        )
        val modern = useCase(
            restoredBoard = board9(Triple(0, 0, 5), Triple(0, 1, 3), Triple(1, 1, 5)),
            type = GameType.DEFAULT9X9,
            initialBoard = initialBoard,
            solvedBoard = solved,
            mistakesMethod = MistakesMethod.MODERN,
        )

        assertTrue(classic[0][0].isLocked)
        assertTrue(classic[1][1].isError)
        assertTrue(modern[1][1].isError)
    }

    @Test
    fun solveBoard_solvesPuzzleAndKeepsOriginalLocks() = runTest(dispatcher.scheduler) {
        val useCase = SolveBoardUseCase(appDispatchers)
        val puzzle =
            "530070000" +
                "600195000" +
                "098000060" +
                "800060003" +
                "400803001" +
                "700020006" +
                "060000280" +
                "000419005" +
                "000080079"
        val initialBoard = puzzle.toBoardList(GameType.DEFAULT9X9).also { it.initiate() }

        val solved = useCase(puzzle, GameType.DEFAULT9X9, initialBoard)

        assertEquals(5, solved[0][0].value)
        assertEquals(4, solved[0][2].value)
        assertTrue(solved[0][0].isLocked)
        assertFalse(solved[0][2].isLocked)
    }

    private fun boardOf(vararg rows: IntArray): List<List<BoardCell>> = rows.mapIndexed { row, values ->
        values.mapIndexed { col, value -> BoardCell(row, col, value) }
    }

    private fun board9(vararg filled: Triple<Int, Int, Int>): List<List<BoardCell>> {
        val board = List(9) { row -> List(9) { col -> BoardCell(row, col, 0) } }
        filled.forEach { (row, col, value) -> board[row][col].value = value }
        return board
    }

    private fun savedGame(uid: Long, status: GameStatus): SavedGame = SavedGame(
        uid = uid,
        board = "board",
        notes = "",
        actions = 0,
        mistakes = 0,
        time = 0L,
        lastPlayed = 0L,
        startedTime = 0L,
        finishedTime = 0L,
        status = status,
    )
}

private class FakeBoardRepository(
    private val board: Board? = null,
) : BoardRepository {
    override suspend fun get(uid: Long): Board? = board?.takeIf { it.uid == uid }

    override suspend fun insert(board: Board): Long = board.uid
}

private class FakeHistoryRepository : HistoryRepository {
    var inserted: History? = null

    override fun getAll(): Flow<List<History>> = emptyFlow()

    override suspend fun insert(history: History): Long {
        inserted = history
        return 1L
    }
}

private class FakeSavedGameRepository(
    var current: SavedGame? = null,
) : SavedGameRepository {
    var inserted: SavedGame? = null
    var updated: SavedGame? = null

    override suspend fun get(uid: Long): SavedGame? = current?.takeIf { it.uid == uid }

    override fun getLast(): Flow<SavedGame> = emptyFlow()

    override suspend fun insert(savedGame: SavedGame): Long {
        inserted = savedGame
        return savedGame.uid
    }

    override suspend fun update(savedGame: SavedGame) {
        updated = savedGame
        current = savedGame
    }

    override suspend fun delete(savedGame: SavedGame) = Unit
}
