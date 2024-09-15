package io.github.japskiddin.sudoku.core.domain

import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow

public interface SavedGameRepository {
    public fun getAll(): Flow<List<SavedGame>>

    public suspend fun get(uid: Long): SavedGame?

    public fun getLast(): Flow<SavedGame?>

    public fun getLastPlayable(limit: Int): Flow<Map<SavedGame, Board>>

    public suspend fun insert(savedGame: SavedGame): Long

    public suspend fun insert(savedGames: List<SavedGame>): List<Long>

    public suspend fun update(savedGame: SavedGame)

    public suspend fun delete(savedGame: SavedGame)
}
