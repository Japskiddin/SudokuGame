package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.data.model.SavedGame
import io.github.japskiddin.sudoku.data.utils.toBoard
import io.github.japskiddin.sudoku.data.utils.toSavedGame
import io.github.japskiddin.sudoku.data.utils.toSavedGameDBO
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class SavedGameRepository
@Inject
constructor(
    private val savedGameDao: SavedGameDao
) {
    public fun getAll(): Flow<List<SavedGame>> =
        savedGameDao.getAll().map { list ->
            list.map { savedGameDBO -> savedGameDBO.toSavedGame() }
        }

    public suspend fun get(uid: Long): SavedGame? = savedGameDao.get(uid)?.toSavedGame()

//    public fun getWithBoards(): Flow<Map<SavedGame, BoardDBO>> = savedGameDao.getSavedWithBoards()

    public fun getLast(): Flow<SavedGame?> = savedGameDao.getLast().map { it.toSavedGame() }

    public fun getLastPlayable(limit: Int): Flow<Map<SavedGame, Board>> =
        savedGameDao.getLastPlayable(limit).map { lastPlayable ->
            lastPlayable.mapKeys { it.key.toSavedGame() }.mapValues { it.value.toBoard() }
        }

    public suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame.toSavedGameDBO())

    public suspend fun insert(savedGames: List<SavedGame>): List<Long> {
        val inserted: MutableList<Long> = mutableListOf()
        savedGames.map { savedGame ->
            savedGame.toSavedGameDBO()
        }.forEach { savedGameDBO ->
            inserted.add(savedGameDao.insert(savedGameDBO))
        }
        return inserted
    }

    public suspend fun update(savedGame: SavedGame): Unit = savedGameDao.update(savedGame.toSavedGameDBO())

    public suspend fun delete(savedGame: SavedGame): Unit = savedGameDao.delete(savedGame.toSavedGameDBO())

    public class SavedGameNotFoundException(message: String) : Exception(message)
}
