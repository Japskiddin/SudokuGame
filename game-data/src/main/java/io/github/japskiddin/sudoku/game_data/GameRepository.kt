package io.github.japskiddin.sudoku.game_data

import io.github.japskiddin.sudoku.database.GameDatabase
import io.github.japskiddin.sudoku.datastore.GameDatastore

class GameRepository(
    private val database: GameDatabase,
    private val datastore: GameDatastore
) {
//    fun getHistory(): Flow<History> {
//    }
}