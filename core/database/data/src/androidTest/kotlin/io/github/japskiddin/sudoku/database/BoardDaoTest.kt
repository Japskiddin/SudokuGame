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
    fun insertBoardsList_returnsTrue() = runBlocking {
        val boards = listOf(
            createDummyBoard(1),
            createDummyBoard(2),
            createDummyBoard(3)
        )

        boardDao.insert(boards)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getAll().collect {
                assertThat(it).isEqualTo(boards)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteBoard_returnsTrue() = runBlocking {
        val board = createDummyBoard(1)

        boardDao.insert(board)
        boardDao.delete(board)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getAll().collect {
                assertThat(it).doesNotContain(board)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun deleteBoardsList_returnsTrue() = runBlocking {
        val boards = listOf(
            createDummyBoard(1),
            createDummyBoard(2),
            createDummyBoard(3)
        )

        boardDao.insert(boards)
        boardDao.delete(boards)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getAll().collect {
                assertThat(it).doesNotContain(boards)
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
            boardDao.getAll().collect {
                assertThat(it).doesNotContain(firstBoard)
                assertThat(it).doesNotContain(secondBoard)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun updateBoard_returnsTrue() = runBlocking {
        val board = createDummyBoard(1)

        boardDao.insert(board)

        val updatedBoard = board.copy(difficulty = 3)

        boardDao.update(updatedBoard)

        val result = boardDao.get(updatedBoard.uid)
        assertThat(result?.difficulty).isEqualTo(updatedBoard.difficulty)
    }

    @Test
    fun updateBoardsList_returnsTrue() = runBlocking {
        val boards = listOf(
            createDummyBoard(1),
            createDummyBoard(2),
            createDummyBoard(3)
        )

        boardDao.insert(boards)

        val updatedBoards = listOf(
            boards[0].copy(difficulty = 3),
            boards[1].copy(difficulty = 4),
            boards[2].copy(difficulty = 1)
        )

        boardDao.update(updatedBoards)

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getAll().collect {
                it.forEachIndexed { index, board ->
                    assertThat(board).isEqualTo(updatedBoards[index])
                }
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun getBoardsWithCurrentDifficulty() = runBlocking {
        val currentDifficulty = 2

        val boards = listOf(
            createDummyBoard(uid = 1, difficulty = 2),
            createDummyBoard(uid = 2, difficulty = 1),
            createDummyBoard(uid = 3, difficulty = 2)
        )

        boardDao.insert(boards)

        val filteredBoards = boards.filter { it.difficulty == currentDifficulty }

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            boardDao.getAll(difficulty = currentDifficulty).collect {
                assertThat(it).isEqualTo(filteredBoards)
                latch.countDown()
            }
        }
        latch.await()
        job.cancelAndJoin()
    }

    private fun createDummyBoard(
        uid: Long,
        initialBoard: String = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        solvedBoard: String = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        difficulty: Int = 2,
        type: Int = 2
    ): BoardDBO = BoardDBO(
        uid = uid,
        initialBoard = initialBoard,
        solvedBoard = solvedBoard,
        difficulty = difficulty,
        type = type
    )
}
