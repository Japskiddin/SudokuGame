package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.AppPreferences
import kotlinx.coroutines.flow.Flow

public interface SettingsRepository {
    public suspend fun setMistakesLimit(enabled: Boolean)

    public fun isMistakesLimit(): Flow<Boolean>

    public suspend fun setShowTimer(enabled: Boolean)

    public fun isShowTimer(): Flow<Boolean>

    public suspend fun setResetTimer(enabled: Boolean)

    public fun isResetTimer(): Flow<Boolean>

    public fun getAppPreferences(): Flow<AppPreferences>
}
