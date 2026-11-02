package io.github.japskiddin.sudoku.feature.home.ui.components

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
import io.github.japskiddin.sudoku.core.ui.resources.continue_game
import io.github.japskiddin.sudoku.core.ui.resources.do_you_want_to_continue
import io.github.japskiddin.sudoku.core.ui.resources.you_already_started_game
import io.github.japskiddin.sudoku.feature.home.ui.resources.Res
import io.github.japskiddin.sudoku.feature.home.ui.resources.ic_start
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

@Composable
internal fun ContinueDialogContent(
    onConfirm: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = stringResource(resource = CoreUiRes.string.you_already_started_game),
            style = SudokuTheme.typography.dialog.copy(
                color = SudokuTheme.colors.onDialog,
                textAlign = TextAlign.Center
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        BasicText(
            text = stringResource(resource = CoreUiRes.string.do_you_want_to_continue),
            style = SudokuTheme.typography.dialog.copy(
                color = SudokuTheme.colors.onDialog,
                textAlign = TextAlign.Center
            ),
        )
        Spacer(modifier = Modifier.height(24.dp))
        GameButton(
            icon = painterResource(resource = Res.drawable.ic_start),
            text = stringResource(resource = CoreUiRes.string.continue_game)
        ) {
            onConfirm()
        }
    }
}

@Preview(
    name = "Continue Dialog",
)
@Composable
private fun ContinueDialogPreview() {
    SudokuTheme {
        ContinueDialogContent(onConfirm = {})
    }
}
