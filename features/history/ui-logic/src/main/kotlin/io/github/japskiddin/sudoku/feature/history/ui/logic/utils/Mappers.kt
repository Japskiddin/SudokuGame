package io.github.japskiddin.sudoku.feature.history.ui.logic.utils

import io.github.japskiddin.sudoku.feature.history.domain.usecase.CombinedHistory
import io.github.japskiddin.sudoku.feature.history.ui.logic.HistoryUI

internal fun CombinedHistory.toHistoryUI(): HistoryUI = HistoryUI(
    uid = this.history.uid,
    time = this.history.time,
    board = this.savedGame.board,
    difficulty = this.board.difficulty,
    type = this.board.type,
    actions = this.savedGame.actions,
    mistakes = this.savedGame.mistakes,
    playTime = this.savedGame.time,
    lastPlayed = this.savedGame.lastPlayed,
    startedAt = this.savedGame.startedTime,
    finishedAt = this.savedGame.finishedTime,
    status = this.savedGame.status,
)
