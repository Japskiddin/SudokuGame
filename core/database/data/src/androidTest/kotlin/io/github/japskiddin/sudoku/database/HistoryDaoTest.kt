package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.entities.HistoryDBO
import io.github.japskiddin.sudoku.database.utils.createDummyBoard
import io.github.japskiddin.sudoku.database.utils.createDummyBoards
import io.github.japskiddin.sudoku.database.utils.createDummyHistory
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
class HistoryDaoTest {
    private lateinit var database: SudokuRoomDatabase
    private lateinit var historyDao: HistoryDao
    private lateinit var boardDao: BoardDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, SudokuRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        historyDao = database.historyDao()
        boardDao = database.boardDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertHistory_returnsTrue() = runTest {
        val boards = createDummyBoards()
        val history = createDummyHistory(1)

        boardDao.insert(boards)
        historyDao.insert(history)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            historyDao.getAll().collect {
                assertThat(it).contains(history)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun insertHistoryList_returnsTrue() = runTest {
        val boards = createDummyBoards()
        val history = listOf(
            createDummyHistory(1),
            createDummyHistory(2),
            createDummyHistory(3)
        )

        boardDao.insert(boards)
        historyDao.insert(history)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            historyDao.getAll().collect {
                assertThat(it).isEqualTo(history)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteHistory_returnsTrue() = runTest {
        val boards = createDummyBoards()
        val history = createDummyHistory(1)

        boardDao.insert(boards)
        historyDao.insert(history)
        historyDao.delete(history)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            historyDao.getAll().collect {
                assertThat(it).doesNotContain(history)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun getAllHistoryWithType_returnsTrue() = runTest {
        val expectedDifficulty = 2
        val expectedType = 1
        val boards = listOf(
            createDummyBoard(uid = 1, difficulty = 2, type = 1),
            createDummyBoard(uid = 2, difficulty = 1, type = 2),
            createDummyBoard(uid = 3, difficulty = 2, type = 1),
        )
        val history = listOf(
            createDummyHistory(1),
            createDummyHistory(2),
            createDummyHistory(3)
        )
        val foundBoards: List<Long> = boards
            .filter {
                it.difficulty == expectedDifficulty && it.type == expectedType
            }
            .map {
                it.uid
            }
        val expectedHistory: List<HistoryDBO> = history.filter {
            it.uid in foundBoards
        }

        boardDao.insert(boards)
        historyDao.insert(history)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            historyDao.getAll(
                difficulty = expectedDifficulty,
                type = expectedType,
            ).collect { history ->
                assertThat(history).isEqualTo(expectedHistory)
            }
        }
        latch.await()
        job.cancelAndJoin()
    }
}
