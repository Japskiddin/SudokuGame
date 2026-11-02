package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import org.jetbrains.skia.Font
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Paint

public actual class BoardTextPaint actual constructor(color: Color, textSize: Float) {
    private val font = Font(
        requireNotNull(
            FontMgr.default.matchFamilyStyleCharacter(null, FontStyle.NORMAL, emptyArray(), '0'.code)
        ),
        textSize,
    )
    private val paint = Paint().apply {
        this.color = color.toArgb()
        isAntiAlias = true
    }

    public actual fun measureText(text: String): Float = font.measureTextWidth(text)

    public actual fun textHeight(text: String): Float = font.measureText(text).height

    public actual fun drawText(canvas: Canvas, text: String, x: Float, y: Float) {
        canvas.nativeCanvas.drawString(text, x, y, font, paint)
    }
}
