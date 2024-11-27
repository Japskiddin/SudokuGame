package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SettingsDataSource
import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.AppPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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

    override fun getAppPreferences(): Flow<AppPreferences> = settingsDataSource.getAppPreferences()
}
