package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.data.model.SavedGame
import io.github.japskiddin.sudoku.data.utils.toSavedGame
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class SavedGameRepository
@Inject
constructor(private val savedGameDao: SavedGameDao) {
    public fun getAll(): Flow<List<SavedGame>> =
        savedGameDao.getAll().map { list ->
            list.map { savedGameDBO -> savedGameDBO.toSavedGame() }
        }

    public suspend fun get(uid: Long): SavedGame? = savedGameDao.get(uid)?.toSavedGame()

    // fun getWithBoards(): Flow<Map<SavedGame, BoardDBO>> = savedGameDao.getSavedWithBoards()

    // fun getLast(): Flow<SavedGame?> = savedGameDao.getLast()

    // fun getLastPlayable(limit: Int): Flow<Map<SavedGame, BoardDBO>> =
    //   savedGameDao.getLastPlayable(limit)

    // suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame)

    // suspend fun insert(savedGames: List<SavedGame>) = savedGameDao.insert(savedGames)

    // suspend fun update(savedGame: SavedGame) = savedGameDao.update(savedGame)

    // suspend fun delete(savedGame: SavedGame) = savedGameDao.delete(savedGame)

    public class SavedGameNotFoundException(message: String) : Exception(message)
}
