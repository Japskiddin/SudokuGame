package io.github.japskiddin.sudoku.core.ui.utils

private const val SecondsInHour = 3600
private const val SecondsInMinute = 60

public fun Long.toFormattedTime(): String {
    val hours = this / SecondsInHour
    val minutes = this % SecondsInHour / SecondsInMinute
    val seconds = this % SecondsInMinute
    fun Long.padded(): String = toString().padStart(2, '0')
    return if (hours > 0) {
        "${hours.padded()}:${minutes.padded()}:${seconds.padded()}"
    } else {
        "${minutes.padded()}:${seconds.padded()}"
    }
}
