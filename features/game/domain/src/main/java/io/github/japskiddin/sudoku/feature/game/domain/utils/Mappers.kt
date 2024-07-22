package io.github.japskiddin.sudoku.feature.game.domain.utils

import io.github.japskiddin.sudoku.core.game.model.BoardCell
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal fun MutableList<MutableList<BoardCell>>.toImmutable(): ImmutableList<ImmutableList<BoardCell>> =
    this.map { item -> item.toImmutableList() }.toImmutableList()

internal fun ImmutableList<ImmutableList<BoardCell>>.toMutable(): MutableList<MutableList<BoardCell>> =
    this.map { item -> item.toMutableList() }.toMutableList()
