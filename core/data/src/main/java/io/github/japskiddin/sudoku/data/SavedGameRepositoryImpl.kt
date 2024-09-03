package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.data.utils.toBoard
import io.github.japskiddin.sudoku.data.utils.toSavedGame
import io.github.japskiddin.sudoku.data.utils.toSavedGameDBO
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class SavedGameRepositoryImpl
@Inject
constructor(
    private val savedGameDao: SavedGameDao
) : SavedGameRepository {
    override fun getAll(): Flow<List<SavedGame>> =
        savedGameDao.getAll().map { list ->
            list.map { savedGameDBO -> savedGameDBO.toSavedGame() }
        }

    override suspend fun get(uid: Long): SavedGame? = savedGameDao.get(uid)?.toSavedGame()

//    public fun getWithBoards(): Flow<Map<SavedGame, BoardDBO>> = savedGameDao.getSavedWithBoards()

    override fun getLast(): Flow<SavedGame?> = savedGameDao.getLast().map { it?.toSavedGame() }

    override fun getLastPlayable(limit: Int): Flow<Map<SavedGame, Board>> =
        savedGameDao.getLastPlayable(limit).map { lastPlayable ->
            lastPlayable.mapKeys { it.key.toSavedGame() }.mapValues { it.value.toBoard() }
        }

    override suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame.toSavedGameDBO())

    override suspend fun insert(savedGames: List<SavedGame>): List<Long> {
        val inserted: MutableList<Long> = mutableListOf()
        savedGames.map { savedGame ->
            savedGame.toSavedGameDBO()
        }.forEach { savedGameDBO ->
            inserted.add(savedGameDao.insert(savedGameDBO))
        }
        return inserted
    }

    override suspend fun update(savedGame: SavedGame): Unit = savedGameDao.update(savedGame.toSavedGameDBO())

    override suspend fun delete(savedGame: SavedGame): Unit = savedGameDao.delete(savedGame.toSavedGameDBO())
}
