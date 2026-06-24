package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameHistoryManagerTest {
    @Test
    fun addState_clearsRedoStackAfterNewBranch() {
        val initial = createHistory(
            value = 1,
        )
        val manager = GameHistoryManager(initial)
        val second = createHistory(
            value = 2,
        )
        val replacement = createHistory(
            value = 3,
        )

        manager.addState(second)
        assertEquals(initial, manager.undo())

        manager.addState(replacement)

        assertNull(manager.redo())
    }

    @Test
    fun undo_returnsPreviousSnapshotWithCounters() {
        val initial = createHistory(
            value = 1,
        )
        val manager = GameHistoryManager(initial)
        val changed = createHistory(
            value = 5,
            notes = listOf(BoardNote(0, 0, 5)),
        )
        manager.addState(changed)

        val restored = manager.undo()

        assertEquals(initial, restored)
    }

    @Test
    fun redo_restoresLatestSnapshotWithCounters() {
        val initial = createHistory(
            value = 1,
        )
        val manager = GameHistoryManager(initial)
        val changed = createHistory(
            value = 7,
            notes = listOf(BoardNote(0, 0, 7)),
        )
        manager.addState(changed)
        manager.undo()

        val restored = manager.redo()

        assertEquals(changed, restored)
    }

    private fun createHistory(
        value: Int,
        notes: List<BoardNote> = emptyList(),
    ): GameHistory = GameHistory(
        board = listOf(listOf(BoardCell(row = 0, col = 0, value = value))),
        notes = notes,
    )
}
