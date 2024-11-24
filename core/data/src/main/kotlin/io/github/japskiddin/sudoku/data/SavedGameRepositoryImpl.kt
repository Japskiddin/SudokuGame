package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SavedGameDataSource
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class SavedGameRepositoryImpl
@Inject
constructor(
    private val savedGameDataSource: SavedGameDataSource
) : SavedGameRepository {
    override fun getAll(): Flow<List<SavedGame>> = savedGameDataSource.getAll()

    override suspend fun get(uid: Long): SavedGame? = savedGameDataSource.get(uid)

//    public fun getWithBoards(): Flow<Map<SavedGame, BoardDBO>> = savedGameDao.getSavedWithBoards()

    override fun getLast(): Flow<SavedGame?> = savedGameDataSource.getLast()

    override fun getLastPlayable(limit: Int): Flow<Map<SavedGame, Board>> =
        savedGameDataSource.getLastPlayable(limit)

    override suspend fun insert(savedGame: SavedGame): Long = savedGameDataSource.insert(savedGame)

    override suspend fun insert(savedGames: List<SavedGame>): List<Long> = savedGameDataSource.insert(savedGames)

    override suspend fun update(savedGame: SavedGame): Unit = savedGameDataSource.update(savedGame)

    override suspend fun delete(savedGame: SavedGame): Unit = savedGameDataSource.delete(savedGame)
}
