package io.github.japskiddin.sudoku.data.utils

import io.github.japskiddin.sudoku.core.model.Record
import io.github.japskiddin.sudoku.database.entities.RecordDBO

internal fun Record.toRecordDBO() = RecordDBO(
    uid = this.uid,
    time = this.time,
)

internal fun RecordDBO.toRecord() = Record(
    uid = this.uid,
    time = this.time,
)
