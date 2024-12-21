package io.github.japskiddin.sudoku.core.model

private const val RADIX: Int = 13

public typealias NoteList = List<BoardNote>

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
public fun String.convertToList(): NoteList {
    val boardNotes: MutableList<BoardNote> = mutableListOf()
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

public fun NoteList.convertToString(): String {
    val sb = StringBuilder()
    forEach {
        sb.append(
            "${it.row.toString(RADIX)},${it.col.toString(RADIX)},${it.value.toString(RADIX)};"
        )
    }
    return sb.toString()
}

private fun boardDigitToInt(char: Char): Int = char.digitToInt(RADIX)
