package io.github.japskiddin.sudoku.core.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

public data class BoardCell(
    val row: Int,
    val col: Int,
    var value: Int = 0,
    var isError: Boolean = false,
    var isLocked: Boolean = false
) {
    public companion object {
        public val Empty: BoardCell = BoardCell(-1, -1)
    }
}

public fun BoardCell.isEmpty(): Boolean = this == BoardCell.Empty

public typealias BoardImmutableList = ImmutableList<ImmutableList<BoardCell>>

public typealias BoardList = List<List<BoardCell>>

public val emptyBoardList: BoardList = List(9) { List(9) { BoardCell.Empty } }

public fun BoardList.toImmutable(): BoardImmutableList = this.map { item -> item.toImmutableList() }.toImmutableList()

public fun BoardImmutableList.toList(): BoardList = this.map { item -> item.toList() }.toList()
