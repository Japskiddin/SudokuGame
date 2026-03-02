package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SavedGameDataSource
import io.github.japskiddin.sudoku.core.model.SavedGame
import io.github.japskiddin.sudoku.data.utils.toSavedGame
import io.github.japskiddin.sudoku.data.utils.toSavedGameDBO
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

public class SavedGameDataSourceImpl
@Inject
constructor(
    private val savedGameDao: SavedGameDao
) : SavedGameDataSource {
    override suspend fun get(uid: Long): SavedGame? = savedGameDao.get(uid)?.toSavedGame()

    override fun getLast(): Flow<SavedGame> = savedGameDao.getLast().map {
        it?.toSavedGame() ?: SavedGame.EMPTY
    }

    override suspend fun insert(savedGame: SavedGame): Long = savedGameDao.insert(savedGame.toSavedGameDBO())

    override suspend fun update(savedGame: SavedGame) {
        savedGameDao.update(savedGame.toSavedGameDBO())
    }

    override suspend fun delete(savedGame: SavedGame) {
        savedGameDao.delete(savedGame.toSavedGameDBO())
    }
}
