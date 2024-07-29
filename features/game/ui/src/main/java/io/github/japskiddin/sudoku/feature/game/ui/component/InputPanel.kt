package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.game.ui.component.autosizetext.AutoSizeText

@Composable
internal fun InputPanel(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    size: Int
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..size) {
            AutoSizeText(
                text = i.toString(),
                alignment = Alignment.Center,
                lineSpacingRatio = 1F,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick(i) }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun InputCell(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    value: String,
    counter: String,
    textSize: TextUnit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick(0) }
    ) {
        Text(
            text = value,
            fontSize = textSize
        )
    }
}

@Preview(
    name = "Input Panel",
    showBackground = true
)
@Composable
private fun InputPanelPreview() {
    InputPanel(size = 9, onClick = {})
}

@Preview(
    name = "Input Cell",
    showBackground = true
)
@Composable
private fun InputCellPreview() {
    SudokuTheme {
        InputCell(
            onClick = {},
            value = "2",
            counter = "3",
            textSize = 16.sp
        )
    }
}
