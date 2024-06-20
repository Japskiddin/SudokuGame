package io.github.japskiddin.sudoku.database.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Entity(
    tableName = "board"
)
data class BoardDBO(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid") val uid: Long,
    @ColumnInfo(name = "initial_board") val initialBoard: String,
    @ColumnInfo(name = "solved_board") val solvedBoard: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "type") val type: Int
)
