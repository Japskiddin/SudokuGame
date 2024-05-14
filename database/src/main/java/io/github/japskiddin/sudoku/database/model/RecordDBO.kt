package io.github.japskiddin.sudoku.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
  tableName = "record",
  foreignKeys = [
    ForeignKey(
      onDelete = ForeignKey.CASCADE,
      entity = SudokuBoardDBO::class,
      parentColumns = arrayOf("uid"),
      childColumns = arrayOf("board_uid"),
    )
  ]
)
data class RecordDBO(
  @PrimaryKey
  @ColumnInfo(name = "board_uid") val boardUid: Long,
  @ColumnInfo(name = "time") val time: Long,
)