package io.github.japskiddin.sudoku.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NoteListAndSavedGameTest {
    @Test
    fun noteList_roundTripsThroughStringRepresentation() {
        val notes = listOf(
            BoardNote(row = 0, col = 1, value = 9),
            BoardNote(row = 10, col = 11, value = 12),
        )

        val serialized = notes.convertToString()

        assertEquals(notes, serialized.convertToList())
    }

    @Test
    fun intArrayConvertToString_usesConfiguredEmptySeparator() {
        assertEquals("1-a", intArrayOf(1, 0, 10).convertToString(emptySeparator = '-'))
    }

    @Test
    fun savedGameCurrentFlags_followUidAndStatus() {
        val currentGame = SavedGame(
            uid = 42L,
            board = "board",
            notes = "",
            actions = 1,
            mistakes = 0,
            time = 10L,
            lastPlayed = 10L,
            startedTime = 10L,
            finishedTime = 0L,
            status = GameStatus.IN_PROGRESS,
        )
        val completedGame = currentGame.copy(status = GameStatus.COMPLETED)

        assertTrue(currentGame.isCurrent())
        assertFalse(currentGame.isNotCurrent())
        assertFalse(completedGame.isCurrent())
        assertTrue(completedGame.isNotCurrent())
    }
}
