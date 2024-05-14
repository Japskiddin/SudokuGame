package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import jakarta.inject.Inject

class GameRepository @Inject constructor(
  private val database: SudokuDatabase,
  private val datastore: SettingsDatastore
) {
}