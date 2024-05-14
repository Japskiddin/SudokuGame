package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.data.utils.toGameLevel
import io.github.japskiddin.sudoku.database.SudokuDatabase
import io.github.japskiddin.sudoku.datastore.SettingsDatastore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository @Inject constructor(
  private val database: SudokuDatabase,
  private val datastore: SettingsDatastore
) {
  fun getGameLevel(): Flow<GameLevel> {
    return datastore.gameLevelFlow.map { it.toGameLevel() }
  }
}