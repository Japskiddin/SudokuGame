package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.dao.RecordDao
import io.github.japskiddin.sudoku.database.entities.RecordDBO
import io.github.japskiddin.sudoku.database.utils.createDummyBoard
import io.github.japskiddin.sudoku.database.utils.createDummyBoards
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
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertRecord_returnsTrue() = runTest {
        val boards = createDummyBoards()
        val record = createDummyRecord(1)

        boardDao.insert(boards)
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

    @Test
    fun insertRecordsList_returnsTrue() = runTest {
        val boards = createDummyBoards()
        val records = listOf(
            createDummyRecord(1),
            createDummyRecord(2),
            createDummyRecord(3)
        )

        boardDao.insert(boards)
        recordDao.insert(records)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            recordDao.getAll().collect {
                assertThat(it).isEqualTo(records)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteRecord_returnsTrue() = runTest {
        val boards = createDummyBoards()
        val record = createDummyRecord(1)

        boardDao.insert(boards)
        recordDao.insert(record)
        recordDao.delete(record)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            recordDao.getAll().collect {
                assertThat(it).doesNotContain(record)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun getAllRecordsWithType_returnsTrue() = runTest {
        val expectedDifficulty = 2
        val expectedType = 1
        val boards = listOf(
            createDummyBoard(uid = 1, difficulty = 2, type = 1),
            createDummyBoard(uid = 2, difficulty = 1, type = 2),
            createDummyBoard(uid = 3, difficulty = 2, type = 1),
        )
        val records = listOf(
            createDummyRecord(1),
            createDummyRecord(2),
            createDummyRecord(3)
        )
        val foundBoards: List<Long> = boards
            .filter {
                it.difficulty == expectedDifficulty && it.type == expectedType
            }
            .map {
                it.uid
            }
        val expectedRecords: List<RecordDBO> = records.filter {
            it.uid in foundBoards
        }

        boardDao.insert(boards)
        recordDao.insert(records)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            recordDao.getAll(
                difficulty = expectedDifficulty,
                type = expectedType,
            ).collect { records ->
                assertThat(records).isEqualTo(expectedRecords)
            }
        }
        latch.await()
        job.cancelAndJoin()
    }
}
