package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.game.model.BoardCell
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun List<List<BoardCell>>.toImmutable(): ImmutableList<ImmutableList<BoardCell>> =
    this.map { item -> item.toImmutableList() }.toImmutableList()

fun ImmutableList<ImmutableList<BoardCell>>.toMutable(): List<List<BoardCell>> =
    this.map { item -> item.toMutableList() }.toMutableList()
