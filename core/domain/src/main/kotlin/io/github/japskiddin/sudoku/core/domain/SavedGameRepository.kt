package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow

public interface SavedGameRepository {
    public suspend fun get(uid: Long): SavedGame?

    public fun getLast(): Flow<SavedGame>

    public suspend fun insert(savedGame: SavedGame): Long

    public suspend fun update(savedGame: SavedGame)

    public suspend fun delete(savedGame: SavedGame)
}
