package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SettingsDataSource
import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.data.utils.toAppPreferences
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Suppress("TooManyFunctions")
public class SettingsDataSourceImpl
@Inject
constructor(
    private val dataStore: SettingsDatastore
) : SettingsDataSource {
    override suspend fun setMistakesLimit(enabled: Boolean) {
        dataStore.setMistakesLimit(enabled)
    }

    override fun isMistakesLimit(): Flow<Boolean> = dataStore.isMistakesLimit

    override suspend fun setShowTimer(enabled: Boolean) {
        dataStore.setShowTimer(enabled)
    }

    override fun isShowTimer(): Flow<Boolean> = dataStore.isShowTimer

    override suspend fun setResetTimer(enabled: Boolean) {
        dataStore.setResetTimer(enabled)
    }

    override fun isResetTimer(): Flow<Boolean> = dataStore.isResetTimer

    override suspend fun setHighlightErrorCells(enabled: Boolean) {
        dataStore.setHighlightErrorCells(enabled)
    }

    override fun isHighlightErrorCells(): Flow<Boolean> = dataStore.isHighlightErrorCells

    override suspend fun setHighlightSimilarCells(enabled: Boolean) {
        dataStore.setHighlightSimilarCells(enabled)
    }

    override fun isHighlightSimilarCells(): Flow<Boolean> = dataStore.isHighlightSimilarCells

    override suspend fun setShowRemainingNumbers(enabled: Boolean) {
        dataStore.setShowRemainingNumbers(enabled)
    }

    override fun isShowRemainingNumbers(): Flow<Boolean> = dataStore.isShowRemainingNumbers

    override suspend fun setHighlightSelectedCell(enabled: Boolean) {
        dataStore.setHighlightSelectedCell(enabled)
    }

    override fun isHighlightSelectedCell(): Flow<Boolean> = dataStore.isHighlightSelectedCell

    override suspend fun setKeepScreenOn(enabled: Boolean) {
        dataStore.setKeepScreenOn(enabled)
    }

    override fun isKeepScreenOn(): Flow<Boolean> = dataStore.isKeepScreenOn

    override suspend fun setSaveLastDifficulty(enabled: Boolean) {
        dataStore.setSaveLastDifficulty(enabled)
    }

    override fun isSaveLastDifficulty(): Flow<Boolean> = dataStore.isSaveLastDifficulty

    override fun getAppPreferences(): Flow<AppPreferences> = dataStore.appPreferences.map { it.toAppPreferences() }
}
