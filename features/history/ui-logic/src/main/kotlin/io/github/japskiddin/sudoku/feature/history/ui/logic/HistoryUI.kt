package io.github.japskiddin.sudoku.feature.history.ui.logic

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.ImmutableBoardList

public data class HistoryUI(
    val uid: Long,
    val time: Long,
    val board: ImmutableBoardList,
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
