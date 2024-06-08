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
    tableName = "saved_game",
    foreignKeys = [
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = BoardDBO::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("board_uid"),
        )
    ]
)
data class SavedGameDBO(
    @PrimaryKey
    @ColumnInfo(name = "board_uid") val uid: Long,
    @ColumnInfo(name = "current_board") val currentBoard: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "actions", defaultValue = "0") val actions: Int = 0,
    @ColumnInfo(name = "mistakes", defaultValue = "0") val mistakes: Int = 0,
    @ColumnInfo(name = "timer", defaultValue = "0") val timer: Long,
    @ColumnInfo(name = "last_played", defaultValue = "0") val lastPlayed: Long,
    @ColumnInfo(name = "started_at", defaultValue = "0") val startedAt: Long,
    @ColumnInfo(name = "finished_at", defaultValue = "0") val finishedAt: Long,
    @ColumnInfo(name = "status", defaultValue = "0") val status: Int = 0,
)
