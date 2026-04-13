package io.github.japskiddin.sudoku.feature.home.domain.usecase

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomeDomainUseCasesTest {
    @Test
    fun deleteSavedGame_deletesOnlyCurrentGame() = runTest {
        val repository = FakeSavedGameRepository()
        val current = savedGame(status = GameStatus.IN_PROGRESS)
        val finished = savedGame(status = GameStatus.COMPLETED)
        val useCase = DeleteSavedGameUseCase(repository)

        useCase(current)
        useCase(finished)

        assertEquals(current, repository.deleted)
    }

    @Test
    fun getGameModePreference_returnsSavedModeOnlyWhenEnabled() = runTest {
        val repository = FakeSettingsRepository(
            gameMode = MutableStateFlow(GameMode(GameDifficulty.HARD, GameType.DEFAULT12X12)),
            isSaveGameMode = MutableStateFlow(true),
        )
        val useCase = GetGameModePreferenceUseCase(repository)

        assertEquals(GameMode(GameDifficulty.HARD, GameType.DEFAULT12X12), useCase().first())

        repository.isSaveGameMode.value = false

        assertEquals(GameMode.Initial, useCase().first())
    }

    private fun savedGame(status: GameStatus): SavedGame = SavedGame(
        uid = 11L,
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

private class FakeSavedGameRepository : SavedGameRepository {
    var deleted: SavedGame? = null

    override suspend fun get(uid: Long): SavedGame? = null

    override fun getLast(): Flow<SavedGame> = flowOf(SavedGame.EMPTY)

    override suspend fun insert(savedGame: SavedGame): Long = savedGame.uid

    override suspend fun update(savedGame: SavedGame) = Unit

    override suspend fun delete(savedGame: SavedGame) {
        deleted = savedGame
    }
}

private class FakeSettingsRepository(
    private val gameMode: MutableStateFlow<GameMode>,
    val isSaveGameMode: MutableStateFlow<Boolean>,
) : SettingsRepository {
    override suspend fun setMistakesLimit(enabled: Boolean) = Unit
    override fun isMistakesLimit(): Flow<Boolean> = flowOf(false)
    override suspend fun setShowTimer(enabled: Boolean) = Unit
    override fun isShowTimer(): Flow<Boolean> = flowOf(false)
    override suspend fun setResetTimer(enabled: Boolean) = Unit
    override fun isResetTimer(): Flow<Boolean> = flowOf(false)
    override suspend fun setHighlightErrorCells(enabled: Boolean) = Unit
    override fun isHighlightErrorCells(): Flow<Boolean> = flowOf(false)
    override suspend fun setHighlightSimilarCells(enabled: Boolean) = Unit
    override fun isHighlightSimilarCells(): Flow<Boolean> = flowOf(false)
    override suspend fun setShowRemainingNumbers(enabled: Boolean) = Unit
    override fun isShowRemainingNumbers(): Flow<Boolean> = flowOf(false)
    override suspend fun setHighlightSelectedCell(enabled: Boolean) = Unit
    override fun isHighlightSelectedCell(): Flow<Boolean> = flowOf(false)
    override suspend fun setKeepScreenOn(enabled: Boolean) = Unit
    override fun isKeepScreenOn(): Flow<Boolean> = flowOf(false)
    override suspend fun setSaveGameMode(enabled: Boolean) = Unit
    override fun isSaveGameMode(): Flow<Boolean> = isSaveGameMode
    override fun getGameMode(): Flow<GameMode> = gameMode
    override suspend fun setGameMode(mode: GameMode) = Unit
    override fun getAppPreferences(): Flow<AppPreferences> = flowOf(
        AppPreferences(
            isMistakesLimit = false,
            isShowTimer = false,
            isResetTimer = false,
            isHighlightErrorCells = false,
            isHighlightSimilarCells = false,
            isShowRemainingNumbers = false,
            isHighlightSelectedCell = false,
            isKeepScreenOn = false,
            isSaveGameMode = false,
        )
    )
}
