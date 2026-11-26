package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.database.utils.createDummyBoard
import io.github.japskiddin.sudoku.database.utils.createDummySavedGame
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SavedGameDaoTest {
    private lateinit var database: SudokuRoomDatabase
    private lateinit var savedGameDao: SavedGameDao
    private lateinit var boardDao: BoardDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, SudokuRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        savedGameDao = database.savedGameDao()
        boardDao = database.boardDao()
        createTestBoards()
    }

    private fun createTestBoards() = runTest {
        boardDao.insert(createDummyBoard(uid = 0))
        boardDao.insert(createDummyBoard(uid = 1))
        boardDao.insert(createDummyBoard(uid = 2))
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertSavedGame_returnsTrue() = runTest {
        val uid = 1L
        val savedGame = createDummySavedGame(uid = uid)

        savedGameDao.insert(savedGame)

        assertThat(savedGameDao.get(uid)).isEqualTo(savedGame)
    }

    @Test
    fun deleteSavedGame_returnsTrue() = runTest {
        val uid = 1L
        val savedGame = createDummySavedGame(uid = uid)

        savedGameDao.insert(savedGame)
        savedGameDao.delete(savedGame)

        assertThat(savedGameDao.get(uid)).isNull()
    }

    @Test
    fun updateSavedGame_returnsTrue() = runTest {
        val savedGame = createDummySavedGame(1)

        savedGameDao.insert(savedGame)

        val updatedSavedGame = savedGame.copy(
            actions = 3,
            mistakes = 5,
        )
        savedGameDao.update(updatedSavedGame)

        val result = savedGameDao.get(updatedSavedGame.uid)
        assertThat(result).isEqualTo(updatedSavedGame)
    }

    @Test
    fun getLastSavedGame_returnsTrue() = runTest {
        val savedGame = createDummySavedGame(1)

        savedGameDao.insert(savedGame)

        assertThat(savedGameDao.getLast().first()).isEqualTo(savedGame)
    }

    @Test
    fun getLastSavedGame_returnsMostRecentInProgressGame() = runTest {
        val activeSavedGame = createDummySavedGame(
            uid = 1,
            lastPlayed = 10L,
            status = 0,
        )
        val completedSavedGame = createDummySavedGame(
            uid = 2,
            lastPlayed = 20L,
            status = 2,
        )

        savedGameDao.insert(activeSavedGame)
        savedGameDao.insert(completedSavedGame)

        assertThat(savedGameDao.getLast().first()).isEqualTo(activeSavedGame)
    }
}
