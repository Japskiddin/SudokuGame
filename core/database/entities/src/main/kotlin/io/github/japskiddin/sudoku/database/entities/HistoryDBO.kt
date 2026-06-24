package io.github.japskiddin.sudoku.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Модель для сохранения сыгранных судоку.
 */
@Keep
@Entity(
    tableName = HistoryDBO.TABLE,
    foreignKeys = [
        ForeignKey(
            entity = BoardDBO::class,
            parentColumns = arrayOf(BoardDBO.COLUMN_UID),
            childColumns = arrayOf(HistoryDBO.COLUMN_BOARD_UID),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
public data class HistoryDBO(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_BOARD_UID) val uid: Long,
    @ColumnInfo(name = COLUMN_TIME) val time: Long
) {
    public companion object {
        public const val TABLE: String = "history"

        public const val COLUMN_BOARD_UID: String = "board_uid"
        public const val COLUMN_TIME: String = "time"
    }
}
