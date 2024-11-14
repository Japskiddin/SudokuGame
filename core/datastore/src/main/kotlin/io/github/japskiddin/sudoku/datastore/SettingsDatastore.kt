package io.github.japskiddin.sudoku.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import io.github.japskiddin.sudoku.datastore.model.AppPreferencesDSO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class SettingsDatastore(applicationContext: Context) {
    private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
    private val dataStore = applicationContext.createDataStore

    private val mistakesLimitKey = booleanPreferencesKey(KEY_MISTAKES_LIMIT)
    private val timerKey = booleanPreferencesKey(KEY_SHOW_TIMER)

    public suspend fun setMistakesLimit(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[mistakesLimitKey] = enabled
        }
    }

    public val isMistakesLimit: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[mistakesLimitKey] ?: DEFAULT_MISTAKES_LIMIT
    }

    public suspend fun setShowTimer(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[timerKey] = enabled
        }
    }

    public val isShowTimer: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[timerKey] ?: DEFAULT_SHOW_TIMER
    }

    public val appPreferences: Flow<AppPreferencesDSO> = dataStore.data.map { preferences ->
        AppPreferencesDSO(
            isMistakesLimit = preferences[mistakesLimitKey] ?: DEFAULT_MISTAKES_LIMIT,
            isShowTimer = preferences[timerKey] ?: DEFAULT_SHOW_TIMER
        )
    }

    private companion object {
        private const val PREFERENCES_NAME = "settings"

        private const val KEY_MISTAKES_LIMIT = "mistakes_limit"
        private const val KEY_SHOW_TIMER = "show_timer"

        private const val DEFAULT_MISTAKES_LIMIT = true
        private const val DEFAULT_SHOW_TIMER = true
    }
}
