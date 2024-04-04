package io.github.japskiddin.sudoku.feature.main.utils

import io.github.japskiddin.sudoku.feature.main.SudokuGenerator

internal fun generateGameBoard(): IntArray {
    val generator = SudokuGenerator(9, 2)
    generator.fillValues()
    val array = IntArray(81)
    var k = 0
    for (i in 0..<9) {
        for (j in 0..<9) {
            array[k] = generator.mat[i][j]
            k++
        }
    }
    return array
}