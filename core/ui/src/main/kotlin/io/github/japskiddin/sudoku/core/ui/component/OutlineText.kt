package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
public fun OutlineText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    fillColor: Color = Color.White,
    outlineColor: Color = Color.Black,
    outlineWidth: Dp = 2.dp
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

    Box {
        Text(
            text = text,
            style = LocalTextStyle.current.merge(outlineTextStyle),
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = text,
            style = LocalTextStyle.current.merge(fillTextStyle),
            modifier = modifier.fillMaxWidth()
        )
    }
}
