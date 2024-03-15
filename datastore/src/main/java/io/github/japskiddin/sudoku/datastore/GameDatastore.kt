package io.github.japskiddin.sudoku.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow

class GameDatastore(applicationContext: Context) {
    private val gameLevelDataStore = applicationContext.gameLevelDatastore

    val gameLevelFlow: Flow<GameLevelDSO>
        get() = gameLevelDataStore.data

    suspend fun clearGameLevel() {
        gameLevelDataStore.updateData { gameLevel ->
            gameLevel.toBuilder()
                .clear()
                .build()
        }
    }

    suspend fun updateGameLevel(
        time: Long? = null,
        board: String? = null,
        actions: Int? = null,
        difficulty: Int? = null
    ) {
        gameLevelDataStore.updateData { gameLevel ->
            val level = GameLevelDSO.newBuilder()
                .setActions(actions ?: gameLevel.actions)
                .setBoard(board ?: gameLevel.board)
                .setTime(time ?: gameLevel.time)
                .setDifficulty(difficulty ?: gameLevel.difficulty)
                .build()

            gameLevel.toBuilder()
                .mergeFrom(level)
                .build()
        }
    }
}