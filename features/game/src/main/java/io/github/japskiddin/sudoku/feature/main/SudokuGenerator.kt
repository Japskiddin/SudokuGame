package io.github.japskiddin.sudoku.feature.main

import kotlin.math.floor
import kotlin.math.sqrt

internal class SudokuGenerator(
    val N: Int,
    val K: Int,
) {
    private val mat: Array<IntArray> = Array(N) { IntArray(N) }
    private val SRN: Int = sqrt(N.toDouble()).toInt()

    fun fillValues() {
        fillDiagonal()
        fillRemaining(0, SRN)
        removeKDigits()
    }

    private fun fillDiagonal() {
        for (i in 0..<N step SRN) {
            fillBox(i, i)
        }
    }

    private fun unUsedInBox(rowStart: Int, colStart: Int, num: Int): Boolean {
        for (i in 0..<SRN) {
            for (j in 0..<SRN) {
                if (mat[rowStart + i][colStart + j] == num) return false
            }
        }
        return true
    }

    private fun fillBox(row: Int, col: Int) {
        var num: Int
        for (i in 0..<SRN) {
            for (j in 0..<SRN) {
                do {
                    num = randomGenerator(N)
                } while (!unUsedInBox(row, col, num))
                mat[row + i][col + j] = num
            }
        }
    }

    private fun randomGenerator(num: Int): Int {
        return floor(Math.random() * num + 1).toInt()
    }

    private fun CheckIfSafe(i: Int, j: Int, num: Int): Boolean {
        return unUsedInRow(i, num)
                && unUsedInCol(j, num)
                && unUsedInBox(i - i % SRN, j - j % SRN, num)
    }

    private fun unUsedInRow(i: Int, num: Int): Boolean {
        for (j in 0..<N) {
            if (mat[i][j] == num) return false
        }
        return true
    }

    private fun unUsedInCol(j: Int, num: Int): Boolean {
        for (i in 0..<N) {
            if (mat[i][j] == num) return false
        }
        return true
    }

    private fun fillRemaining(_i: Int, _j: Int): Boolean {
        var i = _i
        var j = _j
        if (j >= N && i < N - 1) {
            i = i + 1
            j = 0
        }
        if (i >= N && j >= N) {
            return false
        }
        if (i < SRN) {
            if (j < SRN) {
                j = SRN
            }
        } else if (i < N - SRN) {
            if (j == (i / SRN) * SRN) {
                j = j + SRN
            }
        } else {
            if (j == N - SRN) {
                i = i + 1
                j = 0
                if (i >= N) {
                    return false
                }
            }
        }

        for (num in 1..N) {
            if (CheckIfSafe(i, j, num)) {
                mat[i][j] = num
                if (fillRemaining(i, j + 1)) {
                    return true
                }
                mat[i][j] = 0
            }
        }

        return false
    }

    private fun removeKDigits() {
        var count = K
        while (count != 0) {
            val cellId = randomGenerator(N * N) - 1
            val i = cellId / N
            var j = cellId % N
            if (j != 0) {
                j = j - 1
            }
            if (mat[i][j] != 0) {
                count--
                mat[i][j] = 0
            }
        }
    }
}