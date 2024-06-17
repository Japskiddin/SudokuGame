package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DifficultyDialog(
    onDismiss: () -> Unit,
    onStartClick: () -> Unit
) {
    GameDialog(onDismiss = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemSelector(
                items = persistentListOf("Item 1", "Item 2", "Item 3", "Item 4"),
                defaultItemPos = 1
            )
            GameButton(
                icon = null,
                text = stringResource(id = R.string.start),
                onClick = onStartClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(
    name = "Difficulty Dialog"
)
@Composable
private fun DifficultyDialogPreview() {
    SudokuTheme {
        DifficultyDialog(
            onDismiss = {},
            onStartClick = {}
        )
    }
}
