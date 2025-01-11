package io.github.japskiddin.sudoku.feature.history.ui.logic.utils

import io.github.japskiddin.sudoku.core.model.toBoardList
import io.github.japskiddin.sudoku.core.model.toImmutable
import io.github.japskiddin.sudoku.feature.history.domain.usecase.CombinedHistory
import io.github.japskiddin.sudoku.feature.history.ui.logic.HistoryUI

internal fun CombinedHistory.toHistoryUI(): HistoryUI {
    val gameType = this.board.type
    val board = this.savedGame.board.toBoardList(gameType).toImmutable()
    return HistoryUI(
        uid = this.history.uid,
        time = this.history.time,
        board = board,
        difficulty = this.board.difficulty,
        type = gameType,
        actions = this.savedGame.actions,
        mistakes = this.savedGame.mistakes,
        playTime = this.savedGame.time,
        lastPlayed = this.savedGame.lastPlayed,
        startedAt = this.savedGame.startedTime,
        finishedAt = this.savedGame.finishedTime,
        status = this.savedGame.status,
    )
}
