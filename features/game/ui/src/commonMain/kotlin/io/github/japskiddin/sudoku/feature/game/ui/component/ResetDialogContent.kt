package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.resources.ask_reset_current_game
import io.github.japskiddin.sudoku.core.ui.resources.cancel
import io.github.japskiddin.sudoku.core.ui.resources.ic_close
import io.github.japskiddin.sudoku.core.ui.resources.reset
import io.github.japskiddin.sudoku.feature.game.ui.resources.Res
import io.github.japskiddin.sudoku.feature.game.ui.resources.ic_reset
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

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
            text = stringResource(resource = CoreUiRes.string.ask_reset_current_game),
            style = SudokuTheme.typography.dialog.copy(
                color = SudokuTheme.colors.onDialog,
                textAlign = TextAlign.Center
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        GameButton(
            icon = painterResource(resource = Res.drawable.ic_reset),
            text = stringResource(resource = CoreUiRes.string.reset)
        ) {
            onConfirm()
        }
        Spacer(modifier = Modifier.height(6.dp))
        GameButton(
            icon = painterResource(resource = CoreUiRes.drawable.ic_close),
            text = stringResource(resource = CoreUiRes.string.cancel)
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
