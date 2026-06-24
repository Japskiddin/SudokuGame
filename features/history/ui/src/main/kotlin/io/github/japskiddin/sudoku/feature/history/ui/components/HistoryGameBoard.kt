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
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.ImmutableBoardList
import io.github.japskiddin.sudoku.core.model.toImmutable
import io.github.japskiddin.sudoku.core.ui.utils.BoardRadix
import io.github.japskiddin.sudoku.core.ui.utils.drawHorizontalLines
import io.github.japskiddin.sudoku.core.ui.utils.drawVerticalLines
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.random.Random

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun HistoryGameBoard(
    board: ImmutableBoardList,
    size: Int = board.size,
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

        val cellSize by remember(size) {
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
                cellSize = cellSize,
                innerStrokeThickness = horizontalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawVerticalLines(
                boardSize = size,
                cellSize = cellSize,
                innerStrokeThickness = verticalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxWidth,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawNumbers(
                board = board,
                boardSize = size,
                numberPaint = numberPaint,
                cellSize = cellSize
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

private fun DrawScope.drawNumbers(
    board: ImmutableBoardList,
    boardSize: Int,
    numberPaint: TextPaint,
    cellSize: Float,
) {
    for (i in 0 until boardSize) {
        for (j in 0 until boardSize) {
            val cell = board[i][j]
            val number = cell.value
            if (number != 0) {
                drawNumber(
                    cell = cell,
                    number = number,
                    numberPaint = numberPaint,
                    cellSize = cellSize
                )
            }
        }
    }
}

private fun DrawScope.drawNumber(
    cell: BoardCell,
    number: Int,
    numberPaint: TextPaint,
    cellSize: Float,
) {
    val textToDraw = number.toString(BoardRadix).uppercase()
    val textBounds = android.graphics.Rect()
    numberPaint.getTextBounds(textToDraw, 0, 1, textBounds)
    val textWidth = numberPaint.measureText(textToDraw)
    val textPosX = cell.col * cellSize + (cellSize - textWidth) / 2f
    val textPosY = cell.row * cellSize + (cellSize + textBounds.height()) / 2f
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, numberPaint)
    }
}

@Preview(
    name = "History Game Board Preview",
)
@Composable
private fun HistoryGameBoardPreview() {
    @Suppress("MagicNumber")
    val boardSize = 9

    SudokuTheme {
        HistoryGameBoard(
            board = List(boardSize) { row ->
                List(boardSize) { col ->
                    BoardCell(
                        row = row,
                        col = col,
                        value = Random.nextInt(boardSize)
                    )
                }
            }.toImmutable(),
            modifier = Modifier
                .background(Color.White)
                .padding(12.dp)
        )
    }
}
