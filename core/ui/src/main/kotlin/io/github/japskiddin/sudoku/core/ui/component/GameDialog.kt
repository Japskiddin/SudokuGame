package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

@Composable
public fun GameDialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .safeContentPadding()
                    .padding(36.dp)
                    .dialogBackground(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
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
