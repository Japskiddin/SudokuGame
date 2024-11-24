package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme

@Composable
public fun OutlineText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = SudokuTheme.typography.labelMedium,
    fillColor: Color = Color.White,
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 2.dp,
) {
    val fillTextStyle = textStyle.copy(
        color = fillColor,
        textAlign = TextAlign.Center
    )
    val outlineTextStyle = fillTextStyle.copy(
        color = outlineColor,
        drawStyle = Stroke(
            width = with(LocalDensity.current) { outlineWidth.toPx() },
            join = StrokeJoin.Round
        )
    )

    Box(
        modifier = modifier
    ) {
        BasicText(
            text = text,
            style = outlineTextStyle,
            modifier = Modifier.fillMaxWidth()
        )
        BasicText(
            text = text,
            style = fillTextStyle,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun OutlineTextPreview() {
    SudokuTheme {
        OutlineText(
            text = "Text"
        )
    }
}
