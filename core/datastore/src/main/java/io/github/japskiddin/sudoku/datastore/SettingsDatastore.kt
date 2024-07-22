package io.github.japskiddin.sudoku.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class SettingsDatastore(applicationContext: Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = applicationContext.createDataStore

    private val mistakesEnabledKey = booleanPreferencesKey(KEY_MISTAKES_ENABLED)

    public suspend fun setMistakesEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[mistakesEnabledKey] = enabled
        }
    }

    public val isMistakesEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[mistakesEnabledKey] ?: DEFAULT_MISTAKES_ENABLED
    }

    public companion object {
        private const val PREFERENCES_NAME = "settings"

        private const val KEY_MISTAKES_ENABLED = "mistakes_limit"

        private const val DEFAULT_MISTAKES_ENABLED = false
    }
}
