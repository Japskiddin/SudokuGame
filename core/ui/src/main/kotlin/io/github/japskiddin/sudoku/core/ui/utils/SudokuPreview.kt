package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Portrait",
    device = Devices.PIXEL_2,
)
@Preview(
    name = "Landscape",
    widthDp = 732,
    heightDp = 412,
)
public annotation class SudokuPreview
