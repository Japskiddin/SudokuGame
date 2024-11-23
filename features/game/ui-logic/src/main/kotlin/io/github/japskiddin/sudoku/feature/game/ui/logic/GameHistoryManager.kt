package io.github.japskiddin.sudoku.feature.game.ui.logic

import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.BoardNote

internal class GameHistoryManager(
    private val initialState: GameHistory
) {
    private val states: MutableList<GameHistory> = mutableListOf(initialState)
    private val undoStates: MutableList<GameHistory> = mutableListOf()

    fun addState(state: GameHistory) {
        if (state == states.last()) return
        states.add(state)
        undoStates.clear()
    }

    fun undo(): GameHistory = if (canUndo()) {
        states.removeLastOrNull()?.let { undoStates.add(it) }
        states.last()
    } else {
        initialState
    }

    fun redo(): GameHistory? = if (canRedo()) {
        undoStates.removeLastOrNull()?.let { states.add(it) }
        states.last()
    } else {
        null
    }

    fun count() = states.count()

    fun clear() {
        states.clear()
        undoStates.clear()
    }

    private fun canUndo() = states.size > 1

    private fun canRedo() = undoStates.isNotEmpty()
}

internal data class GameHistory(
    val board: BoardList,
    val notes: List<BoardNote>
)
