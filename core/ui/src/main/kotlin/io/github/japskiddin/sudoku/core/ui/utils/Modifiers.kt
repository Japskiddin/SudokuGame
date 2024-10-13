package io.github.japskiddin.sudoku.core.ui.utils

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.DialogSurface

@Composable
public fun Modifier.dialogBackground(): Modifier = this.then(
    Modifier
        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
        .background(
            color = DialogSurface,
            shape = RoundedCornerShape(size = 16.dp)
        )
        .padding(4.dp)
        .innerShadow(
            shape = RoundedCornerShape(size = 12.dp),
            color = Color.Black.copy(alpha = .8f),
            offsetX = 2.dp,
            offsetY = 2.dp
        )
        .innerShadow(
            shape = RoundedCornerShape(size = 12.dp),
            color = Color.White.copy(alpha = .8f),
            offsetX = (-2).dp,
            offsetY = (-2).dp
        )
        .padding(16.dp)
)

@Composable
public fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp = 4.dp,
    offsetY: Dp = 1.dp,
    offsetX: Dp = 1.dp,
    spread: Dp = 0.dp
): Modifier = drawWithContent {
    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint =
        Paint().apply {
            this.color = color
            this.isAntiAlias = true
        }

    val shadowOutline = shape.createOutline(size, layoutDirection, this)

    drawIntoCanvas { canvas ->
        canvas.saveLayer(rect, paint)
        canvas.drawOutline(shadowOutline, paint)

        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }

        paint.color = Color.Black

        val spreadOffsetX = offsetX.toPx() + if (offsetX.toPx() < 0) -spread.toPx() else spread.toPx()
        val spreadOffsetY = offsetY.toPx() + if (offsetY.toPx() < 0) -spread.toPx() else spread.toPx()

        canvas.translate(spreadOffsetX, spreadOffsetY)
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}
