package io.github.japskiddin.sudoku.core.ui.utils

import java.util.*

private const val SecondsInHour = 3600
private const val SecondsInMinute = 60

public fun Long.toFormattedTime(): String {
    val hours = this / SecondsInHour
    val minutes = this % SecondsInHour / SecondsInMinute
    val seconds = this % SecondsInMinute

    return if (hours > 0) {
        "%02d:%02d:%02d".format(Locale.getDefault(), hours, minutes, seconds)
    } else {
        "%02d:%02d".format(Locale.getDefault(), minutes, seconds)
    }
}
