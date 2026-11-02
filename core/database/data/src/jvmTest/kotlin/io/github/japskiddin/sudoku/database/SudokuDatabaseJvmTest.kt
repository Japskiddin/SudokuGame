package io.github.japskiddin.sudoku.database

import io.github.japskiddin.sudoku.database.entities.BoardDBO
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class SudokuDatabaseJvmTest {
    @Test
    fun boardSurvivesReopeningDatabase() = runTest {
        val directory = Files.createTempDirectory("sudoku-database-test").toFile()
        val path = directory.resolve("sudoku").absolutePath
        val board = BoardDBO(uid = 0, board = "1234", solvedBoard = "4321", difficulty = 2, type = 1)
        try {
            val database = SudokuDatabase(path)
            val uid = try {
                database.boardDao.insert(board)
            } finally {
                database.close()
            }
            val reopened = SudokuDatabase(path)
            try {
                assertEquals(board.copy(uid = uid), reopened.boardDao.get(uid))
            } finally {
                reopened.close()
            }
        } finally {
            directory.deleteRecursively()
        }
    }
}
