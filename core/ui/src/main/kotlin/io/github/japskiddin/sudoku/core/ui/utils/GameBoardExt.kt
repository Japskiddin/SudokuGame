package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

public const val BoardRadix: Int = 16

public fun DrawScope.drawHorizontalLines(
    boardSize: Int,
    cellSize: Float,
    innerStrokeThickness: Int,
    outerStrokeColor: Color,
    outerStrokeWidth: Float,
    maxWidth: Float,
    innerStrokeWidth: Float,
    innerStrokeColor: Color,
) {
    for (i in 1 until boardSize) {
        val isOuterStroke = i % innerStrokeThickness == 0
        drawLine(
            color = if (isOuterStroke) outerStrokeColor else innerStrokeColor,
            start = Offset(cellSize * i.toFloat(), 0f),
            end = Offset(cellSize * i.toFloat(), maxWidth),
            strokeWidth = if (isOuterStroke) outerStrokeWidth else innerStrokeWidth
        )
    }
}

public fun DrawScope.drawVerticalLines(
    boardSize: Int,
    cellSize: Float,
    innerStrokeThickness: Int,
    outerStrokeColor: Color,
    outerStrokeWidth: Float,
    maxWidth: Float,
    innerStrokeWidth: Float,
    innerStrokeColor: Color,
) {
    for (i in 1 until boardSize) {
        val isOuterStroke = i % innerStrokeThickness == 0
        if (maxWidth >= cellSize * i) {
            drawLine(
                color = if (isOuterStroke) outerStrokeColor else innerStrokeColor,
                start = Offset(0f, cellSize * i.toFloat()),
                end = Offset(maxWidth, cellSize * i.toFloat()),
                strokeWidth = if (isOuterStroke) outerStrokeWidth else innerStrokeWidth
            )
        }
    }
}
