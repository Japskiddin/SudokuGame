package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.core.model.AppPreferences
import io.github.japskiddin.sudoku.data.utils.toAppPreferences
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class SettingsRepositoryImpl
@Inject
constructor(
    private val dataStore: SettingsDatastore
) : SettingsRepository {
    override suspend fun setMistakesLimit(enabled: Boolean) {
        dataStore.setMistakesLimit(enabled)
    }

    override fun isMistakesLimit(): Flow<Boolean> = dataStore.isMistakesLimit

    override suspend fun setShowTimer(enabled: Boolean) {
        dataStore.setShowTimer(enabled)
    }

    override fun isShowTimer(): Flow<Boolean> = dataStore.isShowTimer

    override fun getAppPreferences(): Flow<AppPreferences> = dataStore.appPreferences.map { it.toAppPreferences() }
}
