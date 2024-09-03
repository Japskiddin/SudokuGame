package io.github.japskiddin.sudoku.database.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Entity(
    tableName = "record",
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = BoardDBO::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("board_uid")
        )
    ]
)
public data class RecordDBO(
    @PrimaryKey
    @ColumnInfo(name = "board_uid") val boardUid: Long,
    @ColumnInfo(name = "time") val time: Long
)
