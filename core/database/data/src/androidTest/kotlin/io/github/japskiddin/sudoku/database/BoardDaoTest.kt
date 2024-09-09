package io.github.japskiddin.sudoku.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.entities.BoardDBO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
@SmallTest
class BoardDaoTest {
    private lateinit var database: SudokuRoomDatabase
    private lateinit var boardDao: BoardDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SudokuRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        boardDao = database.boardDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertBoard_returnsTrue() = runBlocking {
        val board = createDummyBoard(1)
        boardDao.insert(board)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getAll().collect {
                assertThat(it).contains(board)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteAllBoards_returnsTrue() = runBlocking {
        val firstBoard = createDummyBoard(1)
        val secondBoard = createDummyBoard(2)

        boardDao.insert(firstBoard)
        boardDao.insert(secondBoard)

        boardDao.deleteAll()

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getal
        }
    }

    private fun createDummyBoard(uid: Long): BoardDBO = BoardDBO(
        uid = uid,
        initialBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        solvedBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        difficulty = 2,
        type = 2
    )
}
