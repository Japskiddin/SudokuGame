package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

@Composable
public fun GameDialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier.dialogBackground(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview(
    name = "Game Dialog",
    showBackground = true
)
@Composable
private fun GameDialogPreview() {
    SudokuTheme {
        GameDialog(
            onDismiss = {}
        ) {
            Text(text = "This is dialog")
        }
    }
}
