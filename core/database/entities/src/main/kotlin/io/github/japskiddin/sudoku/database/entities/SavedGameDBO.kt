package io.github.japskiddin.sudoku.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Модель для сохранения запущенной игры.
 */
@Keep
@Entity(
    tableName = SavedGameDBO.TABLE,
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = BoardDBO::class,
            parentColumns = arrayOf(BoardDBO.COLUMN_UID),
            childColumns = arrayOf(SavedGameDBO.COLUMN_BOARD_UID)
        )
    ]
)
public data class SavedGameDBO(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_BOARD_UID) val uid: Long,
    @ColumnInfo(name = COLUMN_BOARD) val board: String,
    @ColumnInfo(name = COLUMN_NOTES) val notes: String,
    @ColumnInfo(name = COLUMN_ACTIONS, defaultValue = "0") val actions: Int = 0,
    @ColumnInfo(name = COLUMN_MISTAKES, defaultValue = "0") val mistakes: Int = 0,
    @ColumnInfo(name = COLUMN_TIMER, defaultValue = "0") val timer: Long = 0L,
    @ColumnInfo(name = COLUMN_LAST_PLAYED, defaultValue = "0") val lastPlayed: Long = 0L,
    @ColumnInfo(name = COLUMN_STARTED_TIME, defaultValue = "0") val startedTime: Long = 0L,
    @ColumnInfo(name = COLUMN_FINISHED_TIME, defaultValue = "0") val finishedTime: Long = 0L,
    @ColumnInfo(name = COLUMN_STATUS, defaultValue = "0") val status: Int = 0
) {
    public companion object {
        public const val TABLE: String = "saved_game"

        public const val COLUMN_BOARD_UID: String = "board_uid"
        public const val COLUMN_BOARD: String = "board"
        public const val COLUMN_NOTES: String = "notes"
        public const val COLUMN_ACTIONS: String = "actions"
        public const val COLUMN_MISTAKES: String = "mistakes"
        public const val COLUMN_TIMER: String = "timer"
        public const val COLUMN_LAST_PLAYED: String = "last_played"
        public const val COLUMN_STARTED_TIME: String = "started_time"
        public const val COLUMN_FINISHED_TIME: String = "finished_time"
        public const val COLUMN_STATUS: String = "status"
    }
}
