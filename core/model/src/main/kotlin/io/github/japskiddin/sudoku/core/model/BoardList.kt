package io.github.japskiddin.sudoku.core.model

import io.github.japskiddin.sudoku.core.common.BoardParseException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val RADIX: Int = 13
private val defaultSeparators = listOf('0', '.')

public typealias ImmutableBoardList = ImmutableList<ImmutableList<BoardCell>>

public typealias BoardList = List<List<BoardCell>>

public val emptyBoardList: BoardList = List(9) { List(9) { BoardCell.Empty } }

public fun BoardList.toImmutable(): ImmutableBoardList = map { item -> item.toImmutableList() }.toImmutableList()

public fun ImmutableBoardList.toList(): BoardList = map { item -> item.toList() }.toList()

public fun BoardList.initiate() {
    forEach { cells ->
        cells.forEach { cell ->
            cell.isLocked = cell.value != 0
        }
    }
}

public fun String.toBoardList(
    gameType: GameType,
    locked: Boolean = false,
    emptySeparator: Char? = null
): BoardList {
    if (isEmpty()) {
        throw BoardParseException(message = "Input string is empty")
    }

    val size = gameType.size
    val listBoard = List(size) { row ->
        List(size) { col ->
            BoardCell(row, col, 0)
        }
    }

    forEachIndexed { index, digit ->
        val value = if (emptySeparator != null) {
            if (digit == emptySeparator) 0 else boardDigitToInt(digit)
        } else {
            if (digit in defaultSeparators) 0 else boardDigitToInt(digit)
        }
        val row = index / size
        val col = index % size
        listBoard[row][col].value = value
        listBoard[row][col].isLocked = locked
    }

    return listBoard
}

/**
 * Converts sudoku board to string
 * @return Sudoku in string
 */
@Suppress("MagicNumber", "NestedBlockDepth")
public fun BoardList.convertToString(
    emptySeparator: Char = '0'
): String {
    val sb = StringBuilder()
    forEach { cells ->
        cells.forEach { cell ->
            val value = cell.value
            sb.append(
                if (value <= 9) {
                    if (value != 0) {
                        value.toString()
                    } else {
                        emptySeparator
                    }
                } else {
                    value.toString(RADIX)
                }
            )
        }
    }
    return sb.toString()
}

private fun boardDigitToInt(char: Char): Int = char.digitToInt(RADIX)
