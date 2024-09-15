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
    tableName = "board"
)
public data class BoardDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid") val uid: Long,
    @ColumnInfo(name = "board") val board: String,
    @ColumnInfo(name = "solved_board") val solvedBoard: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "type") val type: Int
)
