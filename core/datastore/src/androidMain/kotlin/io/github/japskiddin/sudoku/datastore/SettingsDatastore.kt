package io.github.japskiddin.sudoku.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

public fun SettingsDatastore(applicationContext: Context): SettingsDatastore =
    SettingsDatastore(applicationContext.applicationContext.settingsDataStore)
