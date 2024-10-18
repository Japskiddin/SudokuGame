package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.getName
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun InfoPanel(
    modifier: Modifier = Modifier,
    gameType: GameType,
    gameDifficulty: GameDifficulty,
    time: Long,
    mistakes: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(
                    CoreUiR.string.current_game_type,
                    gameType.title
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(
                    CoreUiR.string.current_game_difficulty,
                    stringResource(gameDifficulty.getName())
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = time.toString()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(
                    CoreUiR.string.current_mistakes,
                    mistakes.toString()
                )
            )
        }
    }
}

@Preview(
    name = "Info Panel",
    showBackground = true,
    backgroundColor = 0xFFFAA468
)
@Composable
private fun InfoPanelPreview() {
    SudokuTheme {
        InfoPanel(
            gameType = GameType.DEFAULT9X9,
            gameDifficulty = GameDifficulty.INTERMEDIATE,
            time = 0L,
            mistakes = 0
        )
    }
}
