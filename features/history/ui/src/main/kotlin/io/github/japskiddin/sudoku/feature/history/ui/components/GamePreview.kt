@file:Suppress("TooManyFunctions")

package io.github.japskiddin.sudoku.feature.history.ui.components

import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.GameType
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun GamePreview(
    board: String,
    size: Int,
    modifier: Modifier = Modifier,
    outerCornerRadius: Dp = 6.dp,
    outerStrokeWidth: Dp = 0.8.dp,
    innerStrokeWidth: Dp = 0.5.dp,
    numberTextSize: TextUnit = when (size) {
        GameType.DEFAULT6X6.size -> 16.sp
        GameType.DEFAULT9X9.size -> 11.sp
        GameType.DEFAULT12X12.size -> 9.sp
        else -> 22.sp
    },
    numberColor: Color = SudokuTheme.colors.boardNumberNormal,
    outerStrokeColor: Color = Color.Black,
    innerStrokeColor: Color = outerStrokeColor.copy(alpha = 0.2f),
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = SudokuTheme.colors.onPrimary)
    ) {
        val maxWidth = constraints.maxWidth.toFloat()

        val cellSizePx by remember(size) {
            mutableFloatStateOf(maxWidth / size.toFloat())
        }

        val verticalInnerStrokeThickness by remember(size) {
            mutableIntStateOf(floor(sqrt(size.toFloat())).toInt())
        }

        val horizontalInnerStrokeThickness by remember(size) {
            mutableIntStateOf(ceil(sqrt(size.toFloat())).toInt())
        }

        var numberTextSizePx = with(LocalDensity.current) { numberTextSize.toPx() }

        val outerCornerRadiusPx: Float = with(LocalDensity.current) { outerCornerRadius.toPx() }
        val outerStrokeWidthPx = with(LocalDensity.current) { outerStrokeWidth.toPx() }
        val innerStrokeWidthPx: Float = with(LocalDensity.current) { innerStrokeWidth.toPx() }
        val cornerRadius = CornerRadius(outerCornerRadiusPx, outerCornerRadiusPx)

        var numberPaint by remember {
            mutableStateOf(
                TextPaint().apply {
                    color = numberColor.toArgb()
                    isAntiAlias = true
                    textSize = numberTextSizePx
                }
            )
        }

        val context = LocalContext.current
        LaunchedEffect(numberTextSize) {
            numberTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                numberTextSize.value,
                context.resources.displayMetrics
            )
            numberPaint = TextPaint().apply {
                color = numberColor.toArgb()
                isAntiAlias = true
                textSize = numberTextSizePx
            }
        }

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawBoardFrame(
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                cornerRadius = cornerRadius
            )

            drawHorizontalLines(
                boardSize = size,
                cellSize = cellSizePx,
                innerStrokeThickness = horizontalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawVerticalLines(
                boardSize = size,
                cellSize = cellSizePx,
                innerStrokeThickness = verticalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawNumbers(
                size = size,
                board = board,
                numberPaint = numberPaint,
                cellSize = cellSizePx
            )
        }
    }
}

private fun DrawScope.drawBoardFrame(
    outerStrokeColor: Color,
    outerStrokeWidth: Float,
    maxWidth: Float,
    cornerRadius: CornerRadius,
) {
    drawRoundRect(
        color = outerStrokeColor,
        size = Size(maxWidth, maxWidth),
        cornerRadius = cornerRadius,
        style = Stroke(width = outerStrokeWidth)
    )
}

private fun DrawScope.drawHorizontalLines(
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

private fun DrawScope.drawVerticalLines(
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

@Suppress("CyclomaticComplexMethod")
private fun DrawScope.drawNumbers(
    size: Int,
    board: String,
    numberPaint: TextPaint,
    cellSize: Float,
) {
    drawIntoCanvas { canvas ->
        for (i in 0 until size) {
            for (j in 0 until size) {
                val value = board[size * j + i]
                if (value != '0') {
                    val number = value.uppercase()
                    val textBounds = android.graphics.Rect()
                    numberPaint.getTextBounds(number, 0, 1, textBounds)
                    val textWidth = numberPaint.measureText(number)
                    val textHeight = textBounds.height()
                    val textPosX = i * cellSize + (cellSize - textWidth) / 2f
                    val textPosY = j * cellSize + cellSize - (cellSize - textHeight) / 2f
                    canvas.nativeCanvas.drawText(
                        number,
                        textPosX,
                        textPosY,
                        numberPaint
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Game Board Preview",
)
@Composable
private fun GameBoardPreview() {
    SudokuTheme {
        GamePreview(
            board = "0000100000040000000000000700000000000900000000680000000000000005000000000000000",
            size = GameType.DEFAULT9X9.size,
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp)
        )
    }
}
