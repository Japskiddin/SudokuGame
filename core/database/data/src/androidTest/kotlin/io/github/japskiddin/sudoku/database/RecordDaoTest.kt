package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.RecordDao
import io.github.japskiddin.sudoku.database.utils.createBoards
import io.github.japskiddin.sudoku.database.utils.createDummyRecord
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
class RecordDaoTest {
    private lateinit var database: SudokuRoomDatabase
    private lateinit var recordDao: RecordDao
    private lateinit var boardDao: BoardDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, SudokuRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recordDao = database.recordDao()
        boardDao = database.boardDao()
        createTestBoards()
    }

    private fun createTestBoards() {
        val boards = createBoards(1, 2, 3)
        runTest {
            boardDao.insert(boards)
        }
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertRecord_returnsTrue() = runTest {
        val record = createDummyRecord(1)

        recordDao.insert(record)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            recordDao.getAll().collect {
                assertThat(it).contains(record)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }
}
