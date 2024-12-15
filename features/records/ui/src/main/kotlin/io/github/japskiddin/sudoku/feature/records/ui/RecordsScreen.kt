package io.github.japskiddin.sudoku.feature.records.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.records.ui.logic.RecordsViewModel

@Composable
public fun RecordsScreen() {
    RecordsScreen(viewModel = hiltViewModel())
}

@Composable
private fun RecordsScreen(viewModel: RecordsViewModel) {
}

@Composable
private fun RecordsContent() {
}

@Preview(
    name = "Records Content - Portrait mode",
    device = Devices.PIXEL_2,
)
@Preview(
    name = "Records Content - Landscape mode",
    widthDp = 732,
    heightDp = 412,
)
@Composable
private fun RecordsContentPreview() {
    SudokuTheme {
        RecordsContent()
    }
}
