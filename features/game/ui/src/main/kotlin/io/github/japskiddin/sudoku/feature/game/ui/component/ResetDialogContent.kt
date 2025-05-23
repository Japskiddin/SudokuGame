package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.feature.game.ui.R
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun ResetDialogContent(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = stringResource(id = CoreUiR.string.ask_reset_current_game),
            style = SudokuTheme.typography.dialog.copy(
                color = SudokuTheme.colors.onDialog,
                textAlign = TextAlign.Center
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        GameButton(
            icon = painterResource(id = R.drawable.ic_reset),
            text = stringResource(id = CoreUiR.string.reset)
        ) {
            onConfirm()
        }
        Spacer(modifier = Modifier.height(6.dp))
        GameButton(
            icon = painterResource(id = CoreUiR.drawable.ic_close),
            text = stringResource(id = CoreUiR.string.cancel)
        ) {
            onDismiss()
        }
    }
}

@Preview(
    name = "Reset Dialog",
)
@Composable
private fun ResetDialogPreview() {
    SudokuTheme {
        ResetDialogContent(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
