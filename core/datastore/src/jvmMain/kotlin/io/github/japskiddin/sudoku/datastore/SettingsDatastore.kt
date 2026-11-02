package io.github.japskiddin.sudoku.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.File

public fun SettingsDatastore(file: File): SettingsDatastore = SettingsDatastore(
    PreferenceDataStoreFactory.create(produceFile = { file })
)
