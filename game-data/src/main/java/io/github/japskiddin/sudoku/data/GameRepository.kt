package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.data.models.GameLevel
import io.github.japskiddin.sudoku.data.utils.toGameLevel
import io.github.japskiddin.sudoku.database.GameDatabase
import io.github.japskiddin.sudoku.datastore.GameDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val database: GameDatabase,
    private val datastore: GameDatastore
) {
    fun getGameLevel(): Flow<GameLevel> {
        return datastore.gameLevelFlow.map { it.toGameLevel() }
    }
}