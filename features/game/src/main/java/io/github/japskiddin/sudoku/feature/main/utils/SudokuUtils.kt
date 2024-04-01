package io.github.japskiddin.sudoku.feature.main.utils

import io.github.japskiddin.sudoku.feature.main.SudokuGenerator

internal fun generateGameBoard(): IntArray {
    val generator = SudokuGenerator(9, 2)
    generator.fillValues()
    return IntArray(81)
}