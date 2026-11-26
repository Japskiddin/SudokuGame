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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
        val history = createDummyHistory(boardDao.insert(createDummyBoard()))

        historyDao.insert(history)

        assertThat(historyDao.getAll().first()).containsExactly(history)
    }

    @Test
    fun insertHistoryList_returnsTrue() = runTest {
        val firstHistory = createDummyHistory(boardDao.insert(createDummyBoard()))
        val secondHistory = createDummyHistory(boardDao.insert(createDummyBoard()))

        historyDao.insert(firstHistory)
        historyDao.insert(secondHistory)

        assertThat(historyDao.getAll().first()).containsExactly(firstHistory, secondHistory)
    }
}
