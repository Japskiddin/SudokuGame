package io.github.japskiddin.sudoku.feature.records.ui.logic.utils

import io.github.japskiddin.sudoku.core.game.utils.convertToList
import io.github.japskiddin.sudoku.core.model.toImmutable
import io.github.japskiddin.sudoku.feature.records.domain.usecase.CombinedRecord
import io.github.japskiddin.sudoku.feature.records.ui.logic.RecordUI

internal fun CombinedRecord.toRecordUI(): RecordUI = RecordUI(
    uid = this.record.uid,
    time = this.record.time,
    board = this.savedGame.board.convertToList(gameType = this.board.type).toImmutable(),
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
