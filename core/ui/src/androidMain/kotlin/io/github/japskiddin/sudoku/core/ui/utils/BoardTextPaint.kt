package io.github.japskiddin.sudoku.core.ui.utils

import android.graphics.Rect
import android.text.TextPaint
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb

public actual class BoardTextPaint actual constructor(color: Color, textSize: Float) {
    private val paint = TextPaint().apply {
        this.color = color.toArgb()
        this.textSize = textSize
        isAntiAlias = true
    }

    public actual fun measureText(text: String): Float = paint.measureText(text)

    public actual fun textHeight(text: String): Float {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height().toFloat()
    }

    public actual fun drawText(canvas: Canvas, text: String, x: Float, y: Float) {
        canvas.nativeCanvas.drawText(text, x, y, paint)
    }
}
