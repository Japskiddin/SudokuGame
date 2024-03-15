package io.github.japskiddin.sudoku.game_data

import io.github.japskiddin.sudoku.datastore.GameDatastore
import io.github.japskiddin.sudoku.game_data.models.GameLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(
    private val database: GameRoomDatabase,
    private val datastore: GameDatastore
) {
    fun getGameLevel(): Flow<GameLevel> {
        return datastore.gameLevelFlow
            .map { gameLevelDSO ->
                gameLevelDSO.toGameLevel()
            }
    }
}