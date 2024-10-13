package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.OnDialogSurface
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.feature.home.ui.R
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun ContinueDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    GameDialog(onDismiss = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = CoreUiR.string.you_already_started_game),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = OnDialogSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = CoreUiR.string.do_you_want_to_continue),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = OnDialogSurface
            )
            Spacer(modifier = Modifier.height(24.dp))
            GameButton(
                icon = painterResource(id = R.drawable.ic_start),
                text = stringResource(id = CoreUiR.string.continue_game)
            ) {
                onConfirm()
            }
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
            onConfirm = {}
        )
    }
}
