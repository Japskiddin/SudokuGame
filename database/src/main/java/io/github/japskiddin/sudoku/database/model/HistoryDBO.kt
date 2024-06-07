package io.github.japskiddin.sudoku.database.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history")
@Keep
data class HistoryDBO(
  @PrimaryKey(autoGenerate = true) val id: Long,
  @ColumnInfo("date") val date: Date,
  @Embedded(prefix = "level.") val level: Level,
)

@Keep
data class Level(
  @ColumnInfo("playtime") val playtime: Long,
  @ColumnInfo("default_board") val defaultBoard: String,
  @ColumnInfo("current_board") val currentBoard: String,
  @ColumnInfo("completed_board") val completedBoard: String,
  @ColumnInfo("actions") val actions: Int,
  @ColumnInfo("difficulty") val difficulty: Int,
)