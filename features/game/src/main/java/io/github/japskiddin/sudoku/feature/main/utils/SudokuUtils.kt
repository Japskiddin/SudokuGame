package io.github.japskiddin.sudoku.feature.main.utils

import io.github.japskiddin.sudoku.feature.main.SudokuGenerator

internal fun generateGameBoard(): Array<IntArray> {
    val generator = SudokuGenerator(9, 20)
    return generator.fillValues()
//    val array = IntArray(81)
//    var k = 0
//    for (i in 0..8) {
//        for (j in 0..8) {
//            array[k] = generator.mat[i][j]
//            k++
//        }
//    }
//    return array
}