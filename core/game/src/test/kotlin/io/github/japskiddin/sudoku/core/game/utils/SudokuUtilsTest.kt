package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SudokuUtilsTest {
    @Test
    fun sudokuFilledFlags_distinguishEmptyCells() {
        assertTrue("1234".isSudokuFilled())
        assertFalse("1204".isSudokuFilled())
        assertTrue("1204".isSudokuNotFilled())
        assertTrue("".isSudokuNotFilled())
    }

    @Test
    fun isValidCell_comparesAgainstSolvedBoard() {
        val board = boardOf(
            intArrayOf(1, 2),
            intArrayOf(3, 0),
        )
        val solved = boardOf(
            intArrayOf(1, 2),
            intArrayOf(3, 4),
        )

        assertTrue(isValidCell(board, solved, board[1][1]))
        assertFalse(isValidCell(solved, solved, solved[1][1]))
    }

    @Test
    fun isValidCellDynamic_detectsConflictsInRowColumnAndBox() {
        val board = boardOf9(
            Triple(0, 0, 5),
            Triple(0, 4, 5),
            Triple(4, 0, 5),
            Triple(1, 1, 5),
            Triple(4, 4, 7),
        )

        assertFalse(isValidCellDynamic(board, board[0][0], GameType.DEFAULT9X9))
        assertTrue(isValidCellDynamic(board, board[4][4], GameType.DEFAULT9X9))
    }

    @Test
    fun boxRanges_alignWithSectionSize() {
        val cell = BoardCell(row = 5, col = 7, value = 9)

        assertEquals(3..5, getBoxRowRange(cell, sectionHeight = 3))
        assertEquals(6..8, getBoxColRange(cell, sectionWidth = 3))
    }

    private fun boardOf(vararg rows: IntArray): List<List<BoardCell>> = rows.mapIndexed { row, values ->
        values.mapIndexed { col, value -> BoardCell(row, col, value) }
    }

    private fun boardOf9(vararg filled: Triple<Int, Int, Int>): List<List<BoardCell>> {
        val board = List(9) { row -> List(9) { col -> BoardCell(row, col, 0) } }
        filled.forEach { (row, col, value) ->
            board[row][col].value = value
        }
        return board
    }
}
