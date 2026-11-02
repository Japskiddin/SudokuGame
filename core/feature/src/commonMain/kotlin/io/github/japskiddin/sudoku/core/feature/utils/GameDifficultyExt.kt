package io.github.japskiddin.sudoku.core.feature.utils

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.ui.resources.difficulty_easy
import io.github.japskiddin.sudoku.core.ui.resources.difficulty_expert
import io.github.japskiddin.sudoku.core.ui.resources.difficulty_hard
import io.github.japskiddin.sudoku.core.ui.resources.difficulty_intermediate
import io.github.japskiddin.sudoku.core.ui.resources.difficulty_unspecified
import org.jetbrains.compose.resources.StringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

public fun GameDifficulty.getName(): StringResource = when (this) {
    GameDifficulty.UNSPECIFIED -> CoreUiRes.string.difficulty_unspecified
    GameDifficulty.EASY -> CoreUiRes.string.difficulty_easy
    GameDifficulty.INTERMEDIATE -> CoreUiRes.string.difficulty_intermediate
    GameDifficulty.HARD -> CoreUiRes.string.difficulty_hard
    GameDifficulty.EXPERT -> CoreUiRes.string.difficulty_expert
}
