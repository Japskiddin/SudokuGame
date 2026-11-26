package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.getName
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.resources.current_actions
import io.github.japskiddin.sudoku.core.ui.resources.current_mistakes
import io.github.japskiddin.sudoku.core.ui.utils.toFormattedTime
import org.jetbrains.compose.resources.stringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

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

    Row(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = type.title,
                style = textStyle
            )
            Spacer(modifier = Modifier.height(6.dp))
            BasicText(
                text = stringResource(difficulty.getName()),
                style = textStyle
            )
        }
        if (isShowTimer) {
            BasicText(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = time.toFormattedTime(),
                style = textStyle
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            val counterStyle = textStyle.copy(textAlign = TextAlign.End)
            BasicText(
                text = stringResource(
                    CoreUiRes.string.current_actions,
                    actions
                ),
                style = counterStyle
            )
            if (isMistakesLimit) {
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = stringResource(
                        CoreUiRes.string.current_mistakes,
                        mistakes,
                        difficulty.mistakesLimit
                    ),
                    style = counterStyle
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
