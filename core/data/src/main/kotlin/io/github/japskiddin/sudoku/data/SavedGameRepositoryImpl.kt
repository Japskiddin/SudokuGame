package io.github.japskiddin.sudoku.data

import io.github.japskiddin.sudoku.core.domain.SavedGameDataSource
import io.github.japskiddin.sudoku.core.domain.SavedGameRepository
import io.github.japskiddin.sudoku.core.model.SavedGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class SavedGameRepositoryImpl
@Inject
constructor(
    private val savedGameDataSource: SavedGameDataSource
) : SavedGameRepository {
    override suspend fun get(uid: Long): SavedGame? = savedGameDataSource.get(uid)

    override fun getLast(): Flow<SavedGame> = savedGameDataSource.getLast()

    override suspend fun insert(savedGame: SavedGame): Long = savedGameDataSource.insert(savedGame)

    override suspend fun update(savedGame: SavedGame) {
        savedGameDataSource.update(savedGame)
    }

    override suspend fun delete(savedGame: SavedGame) {
        savedGameDataSource.delete(savedGame)
    }
}
