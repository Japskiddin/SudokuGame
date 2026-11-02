package io.github.japskiddin.sudoku.feature.history.ui.utils

import java.text.SimpleDateFormat
import java.util.Locale

internal actual fun Long.toFormattedDate(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)
