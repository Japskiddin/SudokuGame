package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SettingsDataSource
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.core.model.GameMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Suppress("TooManyFunctions")
public class SettingsRepositoryImpl
@Inject
constructor(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository {
    override suspend fun setMistakesLimit(enabled: Boolean): Unit = settingsDataSource.setMistakesLimit(enabled)

    override fun isMistakesLimit(): Flow<Boolean> = settingsDataSource.isMistakesLimit()

    override suspend fun setShowTimer(enabled: Boolean): Unit = settingsDataSource.setShowTimer(enabled)

    override fun isShowTimer(): Flow<Boolean> = settingsDataSource.isShowTimer()

    override suspend fun setResetTimer(enabled: Boolean): Unit = settingsDataSource.setResetTimer(enabled)

    override fun isResetTimer(): Flow<Boolean> = settingsDataSource.isResetTimer()

    override suspend fun setHighlightErrorCells(enabled: Boolean): Unit =
        settingsDataSource.setHighlightErrorCells(enabled)

    override fun isHighlightErrorCells(): Flow<Boolean> = settingsDataSource.isHighlightErrorCells()

    override suspend fun setHighlightSimilarCells(enabled: Boolean): Unit =
        settingsDataSource.setHighlightSimilarCells(enabled)

    override fun isHighlightSimilarCells(): Flow<Boolean> = settingsDataSource.isHighlightSimilarCells()

    override suspend fun setShowRemainingNumbers(enabled: Boolean): Unit =
        settingsDataSource.setShowRemainingNumbers(enabled)

    override fun isShowRemainingNumbers(): Flow<Boolean> = settingsDataSource.isShowRemainingNumbers()

    override suspend fun setHighlightSelectedCell(enabled: Boolean): Unit =
        settingsDataSource.setHighlightSelectedCell(enabled)

    override fun isHighlightSelectedCell(): Flow<Boolean> = settingsDataSource.isHighlightSelectedCell()

    override suspend fun setKeepScreenOn(enabled: Boolean): Unit = settingsDataSource.setKeepScreenOn(enabled)

    override fun isKeepScreenOn(): Flow<Boolean> = settingsDataSource.isKeepScreenOn()

    override suspend fun setSaveGameMode(enabled: Boolean): Unit =
        settingsDataSource.setSaveGameMode(enabled)

    override fun isSaveGameMode(): Flow<Boolean> = settingsDataSource.isSaveGameMode()

    override fun getGameMode(): Flow<GameMode> = settingsDataSource.getGameMode()

    override suspend fun setGameMode(mode: GameMode): Unit =
        settingsDataSource.setGameMode(mode)

    override fun getAppPreferences(): Flow<AppPreferences> = settingsDataSource.getAppPreferences()
}
