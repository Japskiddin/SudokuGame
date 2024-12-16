package io.github.japskiddin.sudoku.feature.records.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType

public data class RecordUI(
    val uid: Long,
    val time: Long,
    val board: String,
    val difficulty: GameDifficulty,
    val type: GameType,
    val actions: Int,
    val mistakes: Int,
    val playTime: Long,
    val lastPlayed: Long,
    val startedAt: Long,
    val finishedAt: Long,
    val status: GameStatus
)
