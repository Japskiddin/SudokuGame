package io.github.japskiddin.sudoku.datastore

import android.content.Context
import io.github.japskiddin.sudoku.datastore.models.GameLevelDO
import io.github.japskiddin.sudoku.datastore.utils.toGameLevelDO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameDatastore(applicationContext: Context) {
    private val gameLevelDataStore = applicationContext.gameLevelDatastore

    val gameLevelFlow: Flow<GameLevelDO>
        get() = gameLevelDataStore.data.map { it.toGameLevelDO() }

    suspend fun clearGameLevel() {
        gameLevelDataStore.updateData { gameLevel ->
            gameLevel.toBuilder()
                .clear()
                .build()
        }
    }

    suspend fun updateGameLevel(
        playtime: Long? = null,
        board: String? = null,
        actions: Int? = null,
        difficulty: Int? = null
    ) {
        gameLevelDataStore.updateData { gameLevel ->
            val level = GameLevelDSO.newBuilder()
                .setActions(actions ?: gameLevel.actions)
                .setBoard(board ?: gameLevel.board)
                .setPlaytime(playtime ?: gameLevel.playtime)
                .setDifficulty(difficulty ?: gameLevel.difficulty)
                .build()

            gameLevel.toBuilder()
                .mergeFrom(level)
                .build()
        }
    }
}