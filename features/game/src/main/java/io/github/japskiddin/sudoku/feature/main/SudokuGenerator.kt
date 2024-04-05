package io.github.japskiddin.sudoku.feature.main

import kotlin.math.floor
import kotlin.math.sqrt

internal class SudokuGenerator(
    private val size: Int,
    private val missingDigits: Int,
) {
    private val items = Array(size) { IntArray(size) }
    private val squareRootOfSize = sqrt(size.toDouble()).toInt()

    fun print() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                print(items[i][j].toString() + " ")
            }
            println()
        }
        println()
    }

    fun fillValues(): Array<IntArray> {
        fillDiagonal()
        fillRemaining(0, squareRootOfSize)
        removeMissingDigits()
        return items
    }

    private fun fillDiagonal() {
        var i = 0
        while (i < size) {
            fillCell(i, i)
            i += squareRootOfSize
        }
    }

    private fun fillCell(row: Int, col: Int) {
        var num: Int
        for (i in 0 until squareRootOfSize) {
            for (j in 0 until squareRootOfSize) {
                do {
                    num = randomGenerator(size)
                } while (!isUnusedInCell(row, col, num))
                items[row + i][col + j] = num
            }
        }
    }

    private fun randomGenerator(num: Int): Int {
        return floor(Math.random() * num + 1).toInt()
    }

    private fun checkIfSafe(i: Int, j: Int, num: Int): Boolean {
        return isUnusedInRow(i, num)
                && isUnusedInCol(j, num)
                && isUnusedInCell(i - i % squareRootOfSize, j - j % squareRootOfSize, num)
    }

    private fun isUnusedInCell(rowStart: Int, colStart: Int, num: Int): Boolean {
        for (i in 0 until squareRootOfSize) {
            for (j in 0 until squareRootOfSize) {
                if (items[rowStart + i][colStart + j] == num) return false
            }
        }
        return true
    }

    private fun isUnusedInRow(i: Int, num: Int): Boolean {
        for (j in 0 until size) {
            if (items[i][j] == num) return false
        }
        return true
    }

    private fun isUnusedInCol(j: Int, num: Int): Boolean {
        for (i in 0 until size) {
            if (items[i][j] == num) return false
        }
        return true
    }

    private fun fillRemaining(startI: Int, startJ: Int): Boolean {
        var i = startI
        var j = startJ
        if (j >= size && i < size - 1) {
            i += 1
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
                j += squareRootOfSize
            }
        } else {
            if (j == size - squareRootOfSize) {
                i += 1
                j = 0
                if (i >= size) {
                    return true
                }
            }
        }

        for (num in 1..size) {
            if (checkIfSafe(i, j, num)) {
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
                j -= 1
            }
            if (items[i][j] != 0) {
                count--
                items[i][j] = 0
            }
        }
    }
}