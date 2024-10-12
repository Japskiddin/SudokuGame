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
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.feature.home.ui.utils.getName
import kotlinx.collections.immutable.persistentListOf
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun DifficultyDialog(
    selectedDifficulty: GameDifficulty,
    selectedType: GameType,
    onDismiss: () -> Unit,
    onStartClick: () -> Unit,
    onSwipeDifficultyLeft: () -> Unit,
    onSwipeDifficultyRight: () -> Unit,
    onSwipeTypeLeft: () -> Unit,
    onSwipeTypeRight: () -> Unit
) {
    GameDialog(onDismiss = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val difficulties = persistentListOf(
                GameDifficulty.EASY,
                GameDifficulty.INTERMEDIATE,
                GameDifficulty.HARD,
                GameDifficulty.EXPERT
            )
            val types = persistentListOf(
                GameType.DEFAULT6X6,
                GameType.DEFAULT9X9,
                GameType.DEFAULT12X12
            )

            ItemSelector(
                currentItem = stringResource(id = selectedDifficulty.getName()),
                itemPos = difficulties.indexOf(selectedDifficulty),
                onSwipeLeft = onSwipeDifficultyLeft,
                onSwipeRight = onSwipeDifficultyRight
            )
            Spacer(modifier = Modifier.height(16.dp))
            ItemSelector(
                currentItem = selectedType.title,
                itemPos = types.indexOf(selectedType),
                onSwipeLeft = onSwipeTypeLeft,
                onSwipeRight = onSwipeTypeRight
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameButton(
                text = stringResource(id = CoreUiR.string.start),
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
            selectedDifficulty = GameDifficulty.EASY,
            selectedType = GameType.DEFAULT9X9,
            onDismiss = {},
            onStartClick = {},
            onSwipeDifficultyLeft = {},
            onSwipeDifficultyRight = {},
            onSwipeTypeLeft = {},
            onSwipeTypeRight = {}
        )
    }
}
