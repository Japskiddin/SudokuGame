package io.github.japskiddin.sudoku.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class SettingsDatastore(applicationContext: Context) {
  companion object {
    private const val PREFERENCES_NAME = "settings"
    private const val MISTAKES_LIMIT = 3
  }

  private val Context.createDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)
  private val dataStore = applicationContext.createDataStore

  private val mistakesLimitKey = booleanPreferencesKey("mistakes_limit")

  suspend fun setMistakesLimit(enabled: Boolean) {
    dataStore.edit { settings ->
      settings[mistakesLimitKey] = enabled
    }
  }

  val mistakesLimit = dataStore.data.map { preferences ->
    preferences[mistakesLimitKey] ?: MISTAKES_LIMIT
  }
}