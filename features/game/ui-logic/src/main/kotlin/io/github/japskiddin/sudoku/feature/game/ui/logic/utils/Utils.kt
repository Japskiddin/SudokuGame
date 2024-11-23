package io.github.japskiddin.sudoku.feature.game.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.BoardList

internal fun copyBoard(board: BoardList): BoardList = board.map { items -> items.map { item -> item.copy() } }
