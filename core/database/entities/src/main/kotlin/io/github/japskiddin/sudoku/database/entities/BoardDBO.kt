package io.github.japskiddin.sudoku.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Модель для сохранения сгенерированных судоку.
 */
@Keep
@Entity(
    tableName = BoardDBO.TABLE
)
public data class BoardDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_UID) val uid: Long,
    @ColumnInfo(name = COLUMN_BOARD) val board: String,
    @ColumnInfo(name = COLUMN_SOLVED_BOARD) val solvedBoard: String,
    @ColumnInfo(name = COLUMN_DIFFICULTY) val difficulty: Int,
    @ColumnInfo(name = COLUMN_TYPE) val type: Int
) {
    public companion object {
        public const val TABLE: String = "board"

        public const val COLUMN_UID: String = "uid"
        public const val COLUMN_BOARD: String = "board"
        public const val COLUMN_SOLVED_BOARD: String = "solved_board"
        public const val COLUMN_DIFFICULTY: String = "difficulty"
        public const val COLUMN_TYPE: String = "type"
    }
}
