package io.github.japskiddin.sudoku.feature.home.ui.utils

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@StringRes
internal fun GameDifficulty.getName(): Int = when (this) {
    GameDifficulty.UNSPECIFIED -> CoreUiR.string.difficulty_unspecified
    GameDifficulty.EASY -> CoreUiR.string.difficulty_easy
    GameDifficulty.INTERMEDIATE -> CoreUiR.string.difficulty_intermediate
    GameDifficulty.HARD -> CoreUiR.string.difficulty_hard
    GameDifficulty.EXPERT -> CoreUiR.string.difficulty_expert
}
