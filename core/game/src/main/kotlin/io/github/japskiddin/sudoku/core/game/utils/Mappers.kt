package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.model.BoardCell
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

public typealias BoardImmutableList = ImmutableList<ImmutableList<BoardCell>>

public typealias BoardList = List<List<BoardCell>>

public val emptyBoardList: BoardList = List(9) { List(9) { BoardCell.Empty } }

public fun BoardList.toImmutable(): BoardImmutableList = this.map { item -> item.toImmutableList() }.toImmutableList()

public fun BoardImmutableList.toList(): BoardList = this.map { item -> item.toList() }.toList()
