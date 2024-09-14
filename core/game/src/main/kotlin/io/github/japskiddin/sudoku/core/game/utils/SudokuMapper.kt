package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import io.github.japskiddin.sudoku.core.model.GameType

private const val RADIX: Int = 13
private val defaultSeparators = listOf('0', '.')

public fun String.convertToList(
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

public fun IntArray.convertToString(
    emptySeparator: Char = '0'
): String {
    val sb = StringBuilder()
    forEach {
        sb.append(
            if (it != 0) {
                it.toString(RADIX)
            } else {
                emptySeparator
            }
        )
    }
    return sb.toString()
}

@Suppress("MagicNumber")
public fun String.convertToList(): List<BoardNote> {
    val boardNotes = mutableListOf<BoardNote>()
    var i = 0
    while (i < length) {
        val index = indexOf(';', i)
        val toParse = substring(i..index)
        val row = boardDigitToInt(toParse[0])
        val col = boardDigitToInt(toParse[2])
        val value = boardDigitToInt(toParse[4])
        boardNotes.add(BoardNote(row, col, value))
        i += index - i + 1
    }
    return boardNotes.toList()
}

public fun List<BoardNote>.convertToString(): String {
    val sb = StringBuilder()
    // row,col,number;row,col,number....row,col,number;
    // e.g 0,3,1;0,3,5;7,7,5;
    forEach {
        sb.append(
            "${it.row.toString(RADIX)},${it.col.toString(RADIX)},${it.value.toString(RADIX)};"
        )
    }
    return sb.toString()
}

private fun boardDigitToInt(char: Char): Int = char.digitToInt(RADIX)
