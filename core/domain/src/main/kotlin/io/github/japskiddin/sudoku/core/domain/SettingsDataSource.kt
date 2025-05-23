package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.core.model.GameMode
import kotlinx.coroutines.flow.Flow

@Suppress("TooManyFunctions")
public interface SettingsDataSource {
    public suspend fun setMistakesLimit(enabled: Boolean)

    public fun isMistakesLimit(): Flow<Boolean>

    public suspend fun setShowTimer(enabled: Boolean)

    public fun isShowTimer(): Flow<Boolean>

    public suspend fun setResetTimer(enabled: Boolean)

    public fun isResetTimer(): Flow<Boolean>

    public suspend fun setHighlightErrorCells(enabled: Boolean)

    public fun isHighlightErrorCells(): Flow<Boolean>

    public suspend fun setHighlightSimilarCells(enabled: Boolean)

    public fun isHighlightSimilarCells(): Flow<Boolean>

    public suspend fun setShowRemainingNumbers(enabled: Boolean)

    public fun isShowRemainingNumbers(): Flow<Boolean>

    public suspend fun setHighlightSelectedCell(enabled: Boolean)

    public fun isHighlightSelectedCell(): Flow<Boolean>

    public suspend fun setKeepScreenOn(enabled: Boolean)

    public fun isKeepScreenOn(): Flow<Boolean>

    public suspend fun setSaveGameMode(enabled: Boolean)

    public fun isSaveGameMode(): Flow<Boolean>

    public fun getGameMode(): Flow<GameMode>

    public suspend fun setGameMode(mode: GameMode)

    public fun getAppPreferences(): Flow<AppPreferences>
}
