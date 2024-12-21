@file:Suppress("TooManyFunctions")

package io.github.japskiddin.sudoku.feature.records.ui.components

import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardImmutableList
import io.github.japskiddin.sudoku.core.model.BoardList
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.toImmutable
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.random.Random

private const val Radix = 16

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun GamePreview(
    board: BoardImmutableList,
    modifier: Modifier = Modifier,
    outerCornerRadius: Dp = 6.dp,
    outerStrokeWidth: Dp = 1.dp,
    innerStrokeWidth: Dp = 0.8.dp,
    numberTextSize: TextUnit = when (board.size) {
        GameType.DEFAULT6X6.size -> 32.sp
        GameType.DEFAULT9X9.size -> 26.sp
        GameType.DEFAULT12X12.size -> 24.sp
        else -> 14.sp
    },
    isDrawBoardFrame: Boolean = false,
    numberColor: Color = SudokuTheme.colors.boardNumberNormal,
    cellColor: Color = SudokuTheme.colors.boardCellNormal,
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
        val boardSize = board.size

        val cellSizePx by remember(boardSize) {
            mutableFloatStateOf(maxWidth / boardSize.toFloat())
        }

        val verticalInnerStrokeThickness by remember(boardSize) {
            mutableIntStateOf(floor(sqrt(boardSize.toFloat())).toInt())
        }

        val horizontalInnerStrokeThickness by remember(boardSize) {
            mutableIntStateOf(ceil(sqrt(boardSize.toFloat())).toInt())
        }

        var numberTextSizePx = with(LocalDensity.current) { numberTextSize.toPx() }

        val outerCornerRadiusPx: Float = with(LocalDensity.current) { outerCornerRadius.toPx() }
        val outerStrokeWidthPx = with(LocalDensity.current) { outerStrokeWidth.toPx() }
        val innerStrokeWidthPx: Float = with(LocalDensity.current) { innerStrokeWidth.toPx() }
        val cornerRadius = CornerRadius(outerCornerRadiusPx, outerCornerRadiusPx)

        val cellSize = Size(cellSizePx, cellSizePx)

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
            // закрашиваем все клетки цветом фона
            for (i in 0 until boardSize) {
                for (j in 0 until boardSize) {
                    val cell = board[i][j]
                    drawCell(
                        cornerRadius = cornerRadius,
                        color = cellColor,
                        boardSize = boardSize,
                        cellSize = cellSize,
                        cellOffset = Offset(
                            x = cell.col * cellSizePx,
                            y = cell.row * cellSizePx
                        )
                    )
                }
            }

            if (isDrawBoardFrame) {
                drawBoardFrame(
                    outerStrokeColor = outerStrokeColor,
                    outerStrokeWidth = outerStrokeWidthPx,
                    maxWidth = maxWidth,
                    cornerRadius = cornerRadius
                )
            }

            drawHorizontalLines(
                boardSize = boardSize,
                cellSize = cellSizePx,
                innerStrokeThickness = horizontalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawVerticalLines(
                boardSize = boardSize,
                cellSize = cellSizePx,
                innerStrokeThickness = verticalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawNumbers(
                boardSize = boardSize,
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

private fun DrawScope.drawCell(
    cornerRadius: CornerRadius,
    color: Color,
    boardSize: Int,
    cellSize: Size,
    cellOffset: Offset,
) {
    val row = (cellOffset.x / cellSize.width).toInt()
    val col = (cellOffset.y / cellSize.height).toInt()
    if (row == 0 && col == 0) {
        drawRoundCellBackground(
            offset = cellOffset,
            size = cellSize,
            color = color,
            topLeftCorner = cornerRadius
        )
    } else if (row == boardSize - 1 && col == 0) {
        drawRoundCellBackground(
            offset = cellOffset,
            size = cellSize,
            color = color,
            topRightCorner = cornerRadius
        )
    } else if (row == 0 && col == boardSize - 1) {
        drawRoundCellBackground(
            offset = cellOffset,
            size = cellSize,
            color = color,
            bottomLeftCorner = cornerRadius
        )
    } else if (row == boardSize - 1 && col == boardSize - 1) {
        drawRoundCellBackground(
            offset = cellOffset,
            size = cellSize,
            color = color,
            bottomRightCorner = cornerRadius
        )
    } else {
        drawCellBackground(
            offset = cellOffset,
            size = cellSize,
            color = color
        )
    }
}

private fun DrawScope.drawCellBackground(
    offset: Offset,
    size: Size,
    color: Color,
) {
    drawRect(
        color = color,
        topLeft = offset,
        size = size
    )
}

private fun DrawScope.drawRoundCellBackground(
    offset: Offset,
    size: Size,
    color: Color,
    topLeftCorner: CornerRadius = CornerRadius.Zero,
    topRightCorner: CornerRadius = CornerRadius.Zero,
    bottomLeftCorner: CornerRadius = CornerRadius.Zero,
    bottomRightCorner: CornerRadius = CornerRadius.Zero,
) {
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = offset,
                    size = size
                ),
                topLeft = topLeftCorner,
                topRight = topRightCorner,
                bottomLeft = bottomLeftCorner,
                bottomRight = bottomRightCorner
            )
        )
    }
    drawPath(path = path, color = color)
}

@Suppress("CyclomaticComplexMethod")
private fun DrawScope.drawNumbers(
    boardSize: Int,
    board: BoardList,
    numberPaint: TextPaint,
    cellSize: Float,
) {
    drawIntoCanvas { canvas ->
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val number = board[i][j]
                if (number.value != 0) {
                    val textToDraw = number.value.toString(Radix).uppercase()
                    val textBounds = android.graphics.Rect()
                    numberPaint.getTextBounds(textToDraw, 0, 1, textBounds)
                    val textWidth = numberPaint.measureText(textToDraw)
                    val textPosX = number.col * cellSize + (cellSize - textWidth) / 2f
                    val textPosY = number.row * cellSize + (cellSize + textBounds.height()) / 2f
                    canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, numberPaint)
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
            board = getSampleBoardForPreview(),
        )
    }
}

private fun getSampleBoardForPreview(size: Int = 9): BoardImmutableList = List(size) { row ->
    List(size) { col ->
        BoardCell(row, col, Random.nextInt(size))
    }
}.toImmutable()
