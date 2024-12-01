package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.getName
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.persistentListOf
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun DifficultyDialog(
    gameMode: GameMode,
    onDismiss: () -> Unit,
    onConfirm: (GameMode) -> Unit,
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

    var difficulty by remember { mutableStateOf(gameMode.difficulty) }
    var type by remember { mutableStateOf(gameMode.type) }

    GameDialog(onDismiss = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ItemSelector(
                currentItem = stringResource(id = difficulty.getName()),
                itemPos = difficulties.indexOf(difficulty),
                onSwipeLeft = {
                    val index = difficulties.indexOf(difficulty)
                    difficulty = if (index <= 0) {
                        difficulties.last()
                    } else {
                        difficulties[index - 1]
                    }
                },
                onSwipeRight = {
                    val index = difficulties.indexOf(difficulty)
                    difficulty = if (index >= difficulties.lastIndex) {
                        difficulties.first()
                    } else {
                        difficulties[index + 1]
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            ItemSelector(
                currentItem = type.title,
                itemPos = types.indexOf(type),
                onSwipeLeft = {
                    val index = types.indexOf(type)
                    type = if (index <= 0) {
                        types.last()
                    } else {
                        types[index - 1]
                    }
                },
                onSwipeRight = {
                    val index = types.indexOf(type)
                    type = if (index >= types.lastIndex) {
                        types.first()
                    } else {
                        types[index + 1]
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            GameButton(
                text = stringResource(id = CoreUiR.string.start),
                icon = painterResource(id = R.drawable.ic_start)
            ) {
                onConfirm(
                    GameMode(
                        difficulty = difficulty,
                        type = type
                    )
                )
            }
        }
    }
}

@Preview(
    name = "Difficulty Dialog",
)
@Composable
private fun DifficultyDialogPreview() {
    SudokuTheme {
        DifficultyDialog(
            gameMode = GameMode.Initial,
            onDismiss = {},
            onConfirm = { _ -> }
        )
    }
}
