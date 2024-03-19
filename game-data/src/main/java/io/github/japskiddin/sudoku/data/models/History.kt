package io.github.japskiddin.sudoku.data.models

import java.util.Date

data class History(
    val id: Long,
    val date: Date,
    val level: GameLevel,
)