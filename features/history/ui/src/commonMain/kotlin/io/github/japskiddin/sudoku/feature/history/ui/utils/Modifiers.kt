package io.github.japskiddin.sudoku.feature.history.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

@Stable
@Composable
internal fun Modifier.cardBackground() = this
    .background(
        color = SudokuTheme.colors.card,
        shape = RoundedCornerShape(12.dp)
    )
    .padding(12.dp)
