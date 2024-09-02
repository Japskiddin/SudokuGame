package io.github.japskiddin.sudoku.feature.home.ui.utils

import androidx.annotation.StringRes
import io.github.japskiddin.sudoku.core.game.GameDifficulty
import io.github.japskiddin.sudoku.feature.home.ui.R

@StringRes
internal fun GameDifficulty.getName(): Int = when (this) {
    GameDifficulty.UNSPECIFIED -> R.string.difficulty_unspecified
    GameDifficulty.EASY -> R.string.difficulty_easy
    GameDifficulty.INTERMEDIATE -> R.string.difficulty_intermediate
    GameDifficulty.HARD -> R.string.difficulty_hard
    GameDifficulty.EXPERT -> R.string.difficulty_expert
}
