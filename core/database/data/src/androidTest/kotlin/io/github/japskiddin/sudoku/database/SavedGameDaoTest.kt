package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.SavedGameDao
import io.github.japskiddin.sudoku.database.utils.createDummyBoards
import io.github.japskiddin.sudoku.database.utils.createDummySavedGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

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

    private fun createTestBoards() {
        val boards = createDummyBoards()
        runTest {
            boardDao.insert(boards)
        }
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertSavedGame_returnsTrue() = runTest {
        val savedGame = createDummySavedGame(1)

        savedGameDao.insert(savedGame)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            savedGameDao.getAll().collect {
                assertThat(it).contains(savedGame)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun insertSavedGamesList_returnsTrue() = runTest {
        val savedGames = listOf(
            createDummySavedGame(1),
            createDummySavedGame(2),
            createDummySavedGame(3),
        )

        savedGameDao.insert(savedGames)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            savedGameDao.getAll().collect {
                assertThat(it).isEqualTo(savedGames)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteSavedGame_returnsTrue() = runTest {
        val savedGame = createDummySavedGame(1)

        savedGameDao.insert(savedGame)
        savedGameDao.delete(savedGame)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            savedGameDao.getAll().collect {
                assertThat(it).doesNotContain(savedGame)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
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
        val savedGames = listOf(
            createDummySavedGame(1),
            createDummySavedGame(2),
            createDummySavedGame(3),
        )

        savedGameDao.insert(savedGames)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            savedGameDao.getLast().collect {
                assertThat(it).isEqualTo(savedGames.last())
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }
}
