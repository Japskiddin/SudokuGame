package io.github.japskiddin.sudoku.game

import androidx.compose.ui.graphics.Color
import io.github.japskiddin.sudoku.core.ui.utils.BoardTextPaint
import kotlin.test.Test
import kotlin.test.assertTrue

class DesktopRenderingJvmTest {
    @Test
    fun boardDigitsHaveVisibleMetrics() {
        val paint = BoardTextPaint(Color.Black, 24f)

        assertTrue(paint.measureText("1") > 0f)
        assertTrue(paint.textHeight("1") > 0f)
    }
}
