package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.model.BoardCell
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

public fun List<List<BoardCell>>.toImmutable(): ImmutableList<ImmutableList<BoardCell>> =
    this.map { item -> item.toImmutableList() }.toImmutableList()

public fun ImmutableList<ImmutableList<BoardCell>>.toList(): List<List<BoardCell>> =
    this.map { item -> item.toList() }.toList()
