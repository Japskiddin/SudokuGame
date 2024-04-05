package io.github.japskiddin.sudoku.feature.main

import kotlin.math.floor
import kotlin.math.sqrt

internal class SudokuGenerator(
    private val size: Int,
    private val missingDigits: Int,
) {
    private val items = Array(size) { IntArray(size) }
    private val squareRootOfSize = sqrt(size.toDouble()).toInt()

    fun fillValues(): Array<IntArray> {
        fillDiagonal()
        fillRemaining(0, squareRootOfSize)
        removeMissingDigits()
        return items
    }

    private fun fillDiagonal() {
        var i = 0
        while (i < size) {
            fillBox(i, i)
            i += squareRootOfSize
        }
    }

    private fun unUsedInBox(rowStart: Int, colStart: Int, num: Int): Boolean {
        for (i in 0 until squareRootOfSize) {
            for (j in 0 until squareRootOfSize) {
                if (items[rowStart + i][colStart + j] == num) return false
            }
        }
        return true
    }

    private fun fillBox(row: Int, col: Int) {
        var num: Int
        for (i in 0 until squareRootOfSize) {
            for (j in 0 until squareRootOfSize) {
                do {
                    num = randomGenerator(size)
                } while (!unUsedInBox(row, col, num))
                items[row + i][col + j] = num
            }
        }
    }

    private fun randomGenerator(num: Int): Int {
        return floor(Math.random() * num + 1).toInt()
    }

    private fun CheckIfSafe(i: Int, j: Int, num: Int): Boolean {
        return unUsedInRow(i, num)
                && unUsedInCol(j, num)
                && unUsedInBox(i - i % squareRootOfSize, j - j % squareRootOfSize, num)
    }

    private fun unUsedInRow(i: Int, num: Int): Boolean {
        for (j in 0 until size) {
            if (items[i][j] == num) return false
        }
        return true
    }

    private fun unUsedInCol(j: Int, num: Int): Boolean {
        for (i in 0 until size) {
            if (items[i][j] == num) return false
        }
        return true
    }

    private fun fillRemaining(i: Int, j: Int): Boolean {
        var i = i
        var j = j
        if (j >= size && i < size - 1) {
            i = i + 1
            j = 0
        }
        if (i >= size && j >= size) {
            return true
        }
        if (i < squareRootOfSize) {
            if (j < squareRootOfSize) {
                j = squareRootOfSize
            }
        } else if (i < size - squareRootOfSize) {
            if (j == (i / squareRootOfSize) * squareRootOfSize) {
                j = j + squareRootOfSize
            }
        } else {
            if (j == size - squareRootOfSize) {
                i = i + 1
                j = 0
                if (i >= size) {
                    return true
                }
            }
        }

        for (num in 1..size) {
            if (CheckIfSafe(i, j, num)) {
                items[i][j] = num
                if (fillRemaining(i, j + 1)) {
                    return true
                }
                items[i][j] = 0
            }
        }

        return false
    }

    private fun removeMissingDigits() {
        var count = missingDigits
        while (count != 0) {
            val cellId = randomGenerator(size * size) - 1
            val i = cellId / size
            var j = cellId % size
            if (j != 0) {
                j = j - 1
            }
            if (items[i][j] != 0) {
                count--
                items[i][j] = 0
            }
        }
    }

    private fun printSudoku() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                print(items[i][j].toString() + " ")
            }
            println()
        }
        println()
    }
}