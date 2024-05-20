package io.github.japskiddin.sudoku.core.game.utils

import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.model.BoardNote
import io.github.japskiddin.sudoku.core.game.qqwing.GameType

// TODO переписать в SudokuMapper
class SudokuParser {
  private val emptySeparators = listOf('0', '.')
  private val radix = 13

  fun parseBoard(
    board: String,
    gameType: GameType,
    locked: Boolean = false,
    emptySeparator: Char? = null
  ): MutableList<MutableList<BoardCell>> {
    if (board.isEmpty()) {
      throw BoardParseException(message = "Input string was empty")
    }

    val size = gameType.size
    val listBoard = MutableList(size) { row ->
      MutableList(size) { col ->
        BoardCell(row, col, 0)
      }
    }

    for (i in board.indices) {
      val value = if (emptySeparator != null) {
        if (board[i] == emptySeparator) 0 else boardDigitToInt(board[i])
      } else {
        if (board[i] in emptySeparators) 0 else boardDigitToInt(board[i])
      }

      listBoard[i / size][i % size].value = value
      listBoard[i / size][i % size].isLocked = locked
    }

    return listBoard
  }

  /**
   * Converts sudoku board to string
   * @param boardList Sudoku board
   * @return Sudoku in string
   */
  fun boardToString(boardList: List<List<BoardCell>>, emptySeparator: Char = '0'): String {
    var boardString = ""
    boardList.forEach { cells ->
      cells.forEach { cell ->
        boardString += if (cell.value <= 9) {
          if (cell.value != 0) {
            cell.value.toString()
          } else {
            emptySeparator
          }
        } else {
          cell.value.toString(radix)
        }
      }
    }
    return boardString
  }

  fun boardToString(board: IntArray, emptySeparator: Char = '0'): String {
    var boardString = ""
    board.forEach {
      boardString += if (it != 0) it.toString(radix) else emptySeparator
    }
    return boardString
  }

  fun parseNotes(notesString: String): List<BoardNote> {
    val boardNotes = mutableListOf<BoardNote>()
    var i = 0
    while (i < notesString.length) {
      println(i.toString())
      val index = notesString.indexOf(';', i)
      val toParse = notesString.substring(i..index)
      val row = boardDigitToInt(toParse[0])
      val col = boardDigitToInt(toParse[2])
      val value = boardDigitToInt(toParse[4])
      boardNotes.add(BoardNote(row, col, value))
      i += index - i + 1
    }
    return boardNotes
  }

  fun notesToString(boardNotes: List<BoardNote>): String {
    var notesString = ""
    // row,col,number;row,col,number....row,col,number;
    // e.g 0,3,1;0,3,5;7,7,5;
    boardNotes.forEach {
      notesString +=
        "${it.row.toString(radix)},${it.col.toString(radix)},${it.value.toString(radix)};"
    }
    return notesString
  }

  private fun boardDigitToInt(char: Char): Int {
    return char.digitToInt(radix)
  }
}

class BoardParseException(message: String) : Exception(message)