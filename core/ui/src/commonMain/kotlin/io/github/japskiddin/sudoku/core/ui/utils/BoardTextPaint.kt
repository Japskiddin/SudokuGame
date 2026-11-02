package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color

public expect class BoardTextPaint(color: Color, textSize: Float) {
    public fun measureText(text: String): Float
    public fun textHeight(text: String): Float
    public fun drawText(canvas: Canvas, text: String, x: Float, y: Float)
}
