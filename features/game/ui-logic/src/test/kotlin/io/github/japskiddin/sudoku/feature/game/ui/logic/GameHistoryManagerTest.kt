package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameHistoryManagerTest {
    @Test
    fun addState_clearsRedoStackAfterNewBranch() {
        val initial = history(
            value = 1,
            actions = 0,
            mistakes = 0,
        )
        val manager = GameHistoryManager(initial)
        val second = history(
            value = 2,
            actions = 1,
            mistakes = 1,
        )
        val replacement = history(
            value = 3,
            actions = 2,
            mistakes = 0,
        )

        manager.addState(second)
        assertEquals(initial, manager.undo())

        manager.addState(replacement)

        assertNull(manager.redo())
    }

    @Test
    fun undo_returnsPreviousSnapshotWithCounters() {
        val initial = history(
            value = 1,
            actions = 0,
            mistakes = 0,
        )
        val manager = GameHistoryManager(initial)
        val changed = history(
            value = 5,
            actions = 4,
            mistakes = 2,
            notes = listOf(BoardNote(0, 0, 5)),
        )
        manager.addState(changed)

        val restored = manager.undo()

        assertEquals(initial, restored)
    }

    @Test
    fun redo_restoresLatestSnapshotWithCounters() {
        val initial = history(
            value = 1,
            actions = 0,
            mistakes = 0,
        )
        val manager = GameHistoryManager(initial)
        val changed = history(
            value = 7,
            actions = 3,
            mistakes = 1,
            notes = listOf(BoardNote(0, 0, 7)),
        )
        manager.addState(changed)
        manager.undo()

        val restored = manager.redo()

        assertEquals(changed, restored)
    }

    private fun history(
        value: Int,
        actions: Int,
        mistakes: Int,
        notes: List<BoardNote> = emptyList(),
    ): GameHistory = GameHistory(
        board = listOf(listOf(BoardCell(row = 0, col = 0, value = value))),
        notes = notes,
        actions = actions,
        mistakes = mistakes,
    )
}
