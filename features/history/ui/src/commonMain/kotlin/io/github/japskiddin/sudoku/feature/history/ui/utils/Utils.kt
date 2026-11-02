package io.github.japskiddin.sudoku.feature.history.ui.utils

import androidx.compose.runtime.Composable
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.ui.resources.completed
import io.github.japskiddin.sudoku.core.ui.resources.failed
import io.github.japskiddin.sudoku.core.ui.resources.game_in_progress
import org.jetbrains.compose.resources.stringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

internal expect fun Long.toFormattedDate(
    pattern: String = "yyyy-MM-dd HH:mm:ss",
): String

@Composable
internal fun GameStatus.toFormattedString(): String = stringResource(
    resource = when (this) {
        GameStatus.COMPLETED -> CoreUiRes.string.completed
        GameStatus.FAILED -> CoreUiRes.string.failed
        GameStatus.IN_PROGRESS -> CoreUiRes.string.game_in_progress
    }
)
