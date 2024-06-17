package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme

@Composable
internal fun DifficultyDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    GameDialog(onDismiss = onDismiss) {

    }
}

@Preview(
    name = "Difficulty Dialog"
)
@Composable
private fun DifficultyDialogPreview() {
    SudokuTheme {
        DifficultyDialog(onDismiss = {})
    }
}
