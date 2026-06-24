package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.HistoryDao
import io.github.japskiddin.sudoku.database.utils.createDummyBoard
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
        val board = createDummyBoard()
        val history = createDummyHistory(1)

        boardDao.insert(board)
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
        val board = createDummyBoard()
        val history = createDummyHistory(1)

        boardDao.insert(board)
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
}
