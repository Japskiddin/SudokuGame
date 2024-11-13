package io.github.japskiddin.sudoku.core.domain

import kotlinx.coroutines.flow.Flow

public interface SettingsRepository {
    public suspend fun setMistakesLimit(enabled: Boolean)

    public fun isMistakesLimitEnabled(): Flow<Boolean>

    public suspend fun setTimer(enabled: Boolean)

    public fun isTimerEnabled(): Flow<Boolean>
}
