package io.github.japskiddin.sudoku.core.model

import io.github.japskiddin.sudoku.core.common.BoardParseException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardListTest {
    @Test
    fun toBoardList_parsesDefaultEmptySeparators() {
        val input =
            "103000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "00000000."
        val board = input.toBoardList(GameType.DEFAULT9X9)

        assertEquals(1, board[0][0].value)
        assertEquals(0, board[0][1].value)
        assertEquals(3, board[0][2].value)
        assertEquals(0, board[8][8].value)
    }

    @Test
    fun toBoardList_parsesCustomSeparatorAndLocksCells() {
        val input =
            "1-0000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "000000000" +
                "00000000-"
        val board = input.toBoardList(
            gameType = GameType.DEFAULT9X9,
            locked = true,
            emptySeparator = '-'
        )

        assertEquals(1, board[0][0].value)
        assertEquals(0, board[0][1].value)
        assertTrue(board[0][0].isLocked)
        assertTrue(board[8][8].isLocked)
    }

    @Test
    fun toBoardList_throwsForEmptyString() {
        assertThrows(BoardParseException::class.java) {
            "".toBoardList(GameType.DEFAULT9X9)
        }
    }

    @Test
    fun convertToString_usesBase13ForValuesAboveNine() {
        val board = listOf(
            listOf(BoardCell(0, 0, 10), BoardCell(0, 1, 11)),
            listOf(BoardCell(1, 0, 12), BoardCell(1, 1, 0))
        )

        assertEquals("abc0", board.convertToString())
    }

    @Test
    fun initiate_locksOnlyFilledCells() {
        val board = listOf(
            listOf(BoardCell(0, 0, 1), BoardCell(0, 1, 0)),
            listOf(BoardCell(1, 0, 0), BoardCell(1, 1, 4))
        )

        board.initiate()

        assertTrue(board[0][0].isLocked)
        assertFalse(board[0][1].isLocked)
        assertFalse(board[1][0].isLocked)
        assertTrue(board[1][1].isLocked)
    }
}
