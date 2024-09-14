package io.github.japskiddin.sudoku.feature.game.ui.logic.utils

import io.github.japskiddin.sudoku.core.game.utils.BoardList

internal fun copyBoard(board: BoardList): BoardList = board.map { items -> items.map { item -> item.copy() } }
