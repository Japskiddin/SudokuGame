package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.game.qqwing.GameDifficulty
import io.github.japskiddin.sudoku.core.game.qqwing.GameType
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DifficultyDialog(
    difficulties: ImmutableList<GameDifficulty>,
    types: ImmutableList<GameType>,
    onDismiss: () -> Unit,
    onStartClick: () -> Unit,
    onSelectedDifficultyChanged: (Int) -> Unit,
    onSelectedTypeChanged: (Int) -> Unit,
) {
    GameDialog(onDismiss = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: придумать, где получать название сложности и типа из enum
            val difficultiesRes = persistentListOf(difficulties).map { it.toStringName() }

            ItemSelector(
                items = persistentListOf("Item 1", "Item 2", "Item 3", "Item 4"),
                defaultItemPos = 1,
                onSelectedItemChanged = onSelectedDifficultyChanged
            )
            Spacer(modifier = Modifier.height(16.dp))
            ItemSelector(
                items = persistentListOf("Item 1", "Item 2", "Item 3", "Item 4"),
                defaultItemPos = 1,
                onSelectedItemChanged = onSelectedTypeChanged
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameButton(
                text = stringResource(id = R.string.start),
                onClick = onStartClick
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
            onStartClick = {},
            onSelectedDifficultyChanged = {},
            onSelectedTypeChanged = {}
        )
    }
}
