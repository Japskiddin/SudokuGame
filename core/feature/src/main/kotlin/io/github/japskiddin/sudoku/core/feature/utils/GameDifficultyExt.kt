package io.github.japskiddin.sudoku.core.feature.utils

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@StringRes
public fun GameDifficulty.getName(): Int = when (this) {
    GameDifficulty.UNSPECIFIED -> CoreUiR.string.difficulty_unspecified
    GameDifficulty.EASY -> CoreUiR.string.difficulty_easy
    GameDifficulty.INTERMEDIATE -> CoreUiR.string.difficulty_intermediate
    GameDifficulty.HARD -> CoreUiR.string.difficulty_hard
    GameDifficulty.EXPERT -> CoreUiR.string.difficulty_expert
}
