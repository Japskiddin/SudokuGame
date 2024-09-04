package io.github.japskiddin.sudoku.database.utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.Date

internal class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { DateFormat.getDateTimeInstance().parse(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): String? {
        return date?.time?.let { DateFormat.getDateTimeInstance().format(it) }
    }
}
