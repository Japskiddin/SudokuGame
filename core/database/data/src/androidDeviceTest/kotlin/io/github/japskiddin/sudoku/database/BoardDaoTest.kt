package io.github.japskiddin.sudoku.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import io.github.japskiddin.sudoku.database.dao.BoardDao
import io.github.japskiddin.sudoku.database.utils.createDummyBoard
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class BoardDaoTest {
    private lateinit var database: SudokuRoomDatabase
    private lateinit var boardDao: BoardDao

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room
            .inMemoryDatabaseBuilder(context, SudokuRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        boardDao = database.boardDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertBoard_returnsTrue() = runTest {
        val uid = 1L
        val board = createDummyBoard(uid = uid)

        boardDao.insert(board)

        assertThat(boardDao.get(uid)).isEqualTo(board)
    }
}
