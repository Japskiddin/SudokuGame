package io.github.japskiddin.sudoku.feature.history.domain.usecase

import io.github.japskiddin.sudoku.core.domain.BoardRepository
import io.github.japskiddin.sudoku.core.domain.HistoryRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetHistoryUseCaseTest {
    @Test
    fun returnsOnlyFullyResolvedEntriesSortedByHistoryTime() = runTest {
        val board = Board(1L, "board", "solved", GameDifficulty.EASY, GameType.DEFAULT9X9)
        val savedGame = SavedGame(
            uid = 1L,
            board = "board",
            notes = "",
            actions = 0,
            mistakes = 0,
            time = 0L,
            lastPlayed = 0L,
            startedTime = 0L,
            finishedTime = 0L,
            status = GameStatus.COMPLETED,
        )
        val useCase = GetHistoryUseCase(
            historyRepository = FlowHistoryRepository(
                listOf(
                    History(uid = 2L, time = 300L),
                    History(uid = 1L, time = 100L),
                )
            ),
            savedGameRepository = StaticSavedGameRepository(savedGame),
            boardRepository = StaticBoardRepository(board),
        )

        val result = useCase().first()

        assertEquals(1, result.size)
        assertEquals(1L, result.single().history.uid)
        assertEquals(board, result.single().board)
        assertEquals(savedGame, result.single().savedGame)
    }
}

private class FlowHistoryRepository(
    private val histories: List<History>,
) : HistoryRepository {
    override fun getAll(): Flow<List<History>> = flowOf(histories)

    override suspend fun insert(history: History): Long = history.uid
}

private class StaticSavedGameRepository(
    private val savedGame: SavedGame?,
) : SavedGameRepository {
    override suspend fun get(uid: Long): SavedGame? = savedGame?.takeIf { it.uid == uid }

    override fun getLast(): Flow<SavedGame> = flowOf(SavedGame.EMPTY)

    override suspend fun insert(savedGame: SavedGame): Long = savedGame.uid

    override suspend fun update(savedGame: SavedGame) = Unit

    override suspend fun delete(savedGame: SavedGame) = Unit
}

private class StaticBoardRepository(
    private val board: Board?,
) : BoardRepository {
    override suspend fun get(uid: Long): Board? = board?.takeIf { it.uid == uid }

    override suspend fun insert(board: Board): Long = board.uid
}
