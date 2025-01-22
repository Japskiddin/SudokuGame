package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.getName
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.utils.toFormattedTime
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun InfoPanel(
    type: GameType,
    difficulty: GameDifficulty,
    time: Long,
    actions: Int,
    mistakes: Int,
    isShowTimer: Boolean,
    isMistakesLimit: Boolean,
    modifier: Modifier = Modifier,
) {
    val textStyle = SudokuTheme.typography.gameInfo.copy(
        color = SudokuTheme.colors.onPrimary
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            BasicText(
                text = type.title,
                style = textStyle
            )
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = stringResource(difficulty.getName()),
                style = textStyle
            )
            if (isShowTimer) {
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = time.toFormattedTime(),
                    style = textStyle
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            BasicText(
                text = stringResource(
                    CoreUiR.string.current_actions,
                    actions
                ),
                style = textStyle
            )
            if (isMistakesLimit) {
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = stringResource(
                        CoreUiR.string.current_mistakes,
                        mistakes,
                        difficulty.mistakesLimit
                    ),
                    style = textStyle
                )
            }
        }
    }
}

@Preview(
    name = "Info Panel",
    showBackground = true,
    backgroundColor = 0xFFFAA468,
)
@Composable
private fun InfoPanelPreview() {
    SudokuTheme {
        InfoPanel(
            type = GameType.DEFAULT9X9,
            difficulty = GameDifficulty.INTERMEDIATE,
            time = 101L,
            actions = 0,
            mistakes = 0,
            isShowTimer = true,
            isMistakesLimit = true
        )
    }
}
