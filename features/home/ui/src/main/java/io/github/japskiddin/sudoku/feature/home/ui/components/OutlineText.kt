package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun OutlineText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: TextUnit = 16.sp,
    fillColor: Color,
    outlineColor: Color,
    outlineWidth: Dp = 2.dp
) {
    Box {
        val fillTextStyle =
            TextStyle(
                color = fillColor,
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        val outlineTextStyle =
            fillTextStyle.copy(
                color = outlineColor,
                drawStyle = Stroke(
                    width = with(LocalDensity.current) { outlineWidth.toPx() },
                    join = StrokeJoin.Round
                )
            )

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
