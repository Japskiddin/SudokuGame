package io.github.japskiddin.sudoku.data.model

import androidx.annotation.Keep
import java.util.Date

@Keep
public data class History(
    val id: Long,
    val date: Date,
    val level: GameLevel
)
