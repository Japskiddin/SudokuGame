package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SettingsRepository
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import kotlinx.coroutines.flow.Flow
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
}
