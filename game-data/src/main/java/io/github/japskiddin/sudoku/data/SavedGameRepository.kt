package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.data.model.SavedGame
import io.github.japskiddin.sudoku.data.utils.toSavedGame
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedGameRepository @Inject constructor(private val savedGameDao: SavedGameDao) {
  fun getAll(): Flow<List<SavedGame>> = savedGameDao.getAll().map { list ->
    list.map { savedGameDBO -> savedGameDBO.toSavedGame() }
  }

  suspend fun get(uid: Long): SavedGame = savedGameDao.get(uid)?.toSavedGame()
    ?: throw SavedGameNotFoundException("Saved game with $uid not found!")

  // fun getWithBoards(): Flow<Map<SavedGame, BoardDBO>> = savedGameDao.getSavedWithBoards()

  // fun getLast(): Flow<SavedGame?> = savedGameDao.getLast()

  // fun getLastPlayable(limit: Int): Flow<Map<SavedGame, BoardDBO>> =
  //   savedGameDao.getLastPlayable(limit)

  // suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame)

  // suspend fun insert(savedGames: List<SavedGame>) = savedGameDao.insert(savedGames)

  // suspend fun update(savedGame: SavedGame) = savedGameDao.update(savedGame)

  // suspend fun delete(savedGame: SavedGame) = savedGameDao.delete(savedGame)

  class SavedGameNotFoundException(message: String) : Exception(message)
}