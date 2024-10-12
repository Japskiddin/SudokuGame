package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.OnDialogSurface
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun ContinueDialog(
    onDismiss: () -> Unit,
    onContinueClick: () -> Unit
) {
    GameDialog(onDismiss = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = CoreUiR.string.you_already_started_game),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = OnDialogSurface
            )
            Text(
                text = stringResource(id = CoreUiR.string.do_you_want_to_continue),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = OnDialogSurface,
                modifier = Modifier.padding(top = 16.dp)
            )
            GameButton(
                icon = null,
                text = stringResource(id = CoreUiR.string.continue_game),
                onClick = onContinueClick,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(
    name = "Continue Dialog"
)
@Composable
private fun ContinueDialogPreview() {
    SudokuTheme {
        ContinueDialog(
            onDismiss = {},
            onContinueClick = {}
        )
    }
}
