package io.github.japskiddin.sudoku.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history")
data class HistoryDBO(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("date") val date: Date,
    @Embedded(prefix = "level.") val level: Level,
)

data class Level(
    @ColumnInfo("time") val time: Long,
    @ColumnInfo("board") val board: String,
    @ColumnInfo("completed_board") val completedBoard: String,
    @ColumnInfo("actions") val actions: Int,
    @ColumnInfo("difficulty") val difficulty: Int,
)