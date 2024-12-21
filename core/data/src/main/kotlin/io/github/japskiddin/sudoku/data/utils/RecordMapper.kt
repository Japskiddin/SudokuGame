package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.model.History
import io.github.japskiddin.sudoku.database.entities.HistoryDBO

internal fun History.toHistoryDBO() = HistoryDBO(
    uid = this.uid,
    time = this.time,
)

internal fun HistoryDBO.toHistory() = History(
    uid = this.uid,
    time = this.time,
)
