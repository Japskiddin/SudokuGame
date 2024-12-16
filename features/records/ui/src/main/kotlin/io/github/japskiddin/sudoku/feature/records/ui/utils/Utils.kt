package io.github.japskiddin.sudoku.feature.records.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import io.github.japskiddin.sudoku.core.model.GameStatus
import java.text.SimpleDateFormat
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

internal fun Long.toFormattedDate(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
): String {
    val sdf = SimpleDateFormat(pattern, Locale.current.platformLocale)
    return sdf.format(this)
}

@Composable
internal fun GameStatus.toFormattedString(): String = stringResource(
    id = when (this) {
        GameStatus.COMPLETED -> CoreUiR.string.completed
        GameStatus.FAILED -> CoreUiR.string.failed
        GameStatus.IN_PROGRESS -> CoreUiR.string.in_progress
    }
)
