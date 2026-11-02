package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.skiaPaint
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter

@Composable
public actual fun Modifier.innerShadow(
    shape: Shape,
    color: Color,
    blur: Dp,
    offsetY: Dp,
    offsetX: Dp,
    spread: Dp,
): Modifier = drawWithContent {
    drawContent()
    val paint = Paint().apply {
        this.color = color
        isAntiAlias = true
    }
    val outline = shape.createOutline(size, layoutDirection, this)
    drawIntoCanvas { canvas ->
        canvas.saveLayer(Rect(Offset.Zero, size), paint)
        canvas.drawOutline(outline, paint)
        paint.blendMode = BlendMode.DstOut
        if (blur.toPx() > 0) {
            paint.skiaPaint.maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, blur.toPx() / 2)
        }
        paint.color = Color.Black
        canvas.translate(
            offsetX.toPx() + if (offsetX.value < 0) -spread.toPx() else spread.toPx(),
            offsetY.toPx() + if (offsetY.value < 0) -spread.toPx() else spread.toPx(),
        )
        canvas.drawOutline(outline, paint)
        canvas.restore()
    }
}
