package io.github.japskiddin.sudoku.feature.game.domain.utils

import io.github.japskiddin.sudoku.core.game.model.BoardCell

internal fun copyBoard(board: List<List<BoardCell>>): List<List<BoardCell>> =
    board.map { items -> items.map { item -> item.copy() } }
