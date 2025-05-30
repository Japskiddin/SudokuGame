@file:Suppress("TooManyFunctions")

package io.github.japskiddin.sudoku.feature.game.ui.component

import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.BoardNote
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.ImmutableBoardList
import io.github.japskiddin.sudoku.core.model.toImmutable
import io.github.japskiddin.sudoku.core.ui.utils.BoardRadix
import io.github.japskiddin.sudoku.core.ui.utils.drawHorizontalLines
import io.github.japskiddin.sudoku.core.ui.utils.drawVerticalLines
import io.github.japskiddin.sudoku.core.ui.utils.innerShadow
import io.github.japskiddin.sudoku.feature.game.ui.utils.findGameTypeBySize
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.random.Random

private const val MinZoomRange = 1f
private const val MaxZoomRange = 3f

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun GameBoard(
    board: ImmutableBoardList,
    modifier: Modifier = Modifier,
    size: Int = board.size,
    selectedCell: BoardCell = BoardCell.Empty,
    gameType: GameType = findGameTypeBySize(size),
    outerCornerRadius: Dp = 12.dp,
    outerStrokeWidth: Dp = 1.5.dp,
    innerStrokeWidth: Dp = 1.dp,
    numberTextSize: TextUnit = when (gameType) {
        GameType.DEFAULT6X6 -> 32.sp
        GameType.DEFAULT9X9 -> 26.sp
        GameType.DEFAULT12X12 -> 24.sp
        else -> 14.sp
    },
    noteTextSize: TextUnit = when (gameType) {
        GameType.DEFAULT6X6 -> 18.sp
        GameType.DEFAULT9X9 -> 12.sp
        GameType.DEFAULT12X12 -> 7.sp
        else -> 14.sp
    },
    isIdenticalNumbersHighlight: Boolean = true,
    isErrorsHighlight: Boolean = true,
    isPositionCells: Boolean = true,
    isEnabled: Boolean = true,
    isRenderNotes: Boolean = true,
    isZoomable: Boolean = false,
    notes: List<BoardNote> = emptyList(),
    numberColor: Color = SudokuTheme.colors.boardNumberNormal,
    selectedNumberColor: Color = SudokuTheme.colors.boardNumberSelected,
    lockedNumberColor: Color = SudokuTheme.colors.boardNumberLocked,
    errorNumberColor: Color = SudokuTheme.colors.boardNumberError,
    cellColor: Color = SudokuTheme.colors.boardCellNormal,
    selectedCellColor: Color = SudokuTheme.colors.boardCellSelected,
    noteColor: Color = numberColor.copy(alpha = 0.8f),
    outerStrokeColor: Color = Color.Black,
    innerStrokeColor: Color = outerStrokeColor.copy(alpha = 0.2f),
    onSelectCell: (BoardCell) -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .boardBackground()
    ) {
        val maxSize = constraints.maxWidth.toFloat()

        val cellSizePx = maxSize / size.toFloat()

        val sqrtSize = sqrt(size.toFloat())
        val ceilSqrtSize = ceil(sqrtSize)
        val floorSqrtSize = floor(sqrtSize)

        val cellSizeDividerWidth = cellSizePx / ceilSqrtSize
        val cellSizeDividerHeight = cellSizePx / floorSqrtSize

        val verticalInnerStrokeThickness = floorSqrtSize.toInt()
        val horizontalInnerStrokeThickness = ceilSqrtSize.toInt()

        val density = LocalDensity.current

        var numberTextSizePx = with(density) { numberTextSize.toPx() }
        var noteTextSizePx = with(density) { noteTextSize.toPx() }

        val outerCornerRadiusPx = with(density) { outerCornerRadius.toPx() }
        val outerStrokeWidthPx = with(density) { outerStrokeWidth.toPx() }
        val innerStrokeWidthPx = with(density) { innerStrokeWidth.toPx() }
        val cornerRadius = CornerRadius(outerCornerRadiusPx, outerCornerRadiusPx)

        var zoom by remember(isEnabled) { mutableFloatStateOf(1f) }
        var offset by remember(isEnabled) { mutableStateOf(Offset.Zero) }

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
        var selectedNumberPaint by remember {
            mutableStateOf(
                TextPaint().apply {
                    color = selectedNumberColor.toArgb()
                    isAntiAlias = true
                    textSize = numberTextSizePx
                }
            )
        }
        var errorNumberPaint by remember {
            mutableStateOf(
                TextPaint().apply {
                    color = errorNumberColor.toArgb()
                    isAntiAlias = true
                    textSize = numberTextSizePx
                }
            )
        }
        var lockedNumberPaint by remember {
            mutableStateOf(
                TextPaint().apply {
                    color = lockedNumberColor.toArgb()
                    isAntiAlias = true
                    textSize = numberTextSizePx
                }
            )
        }
        var notePaint by remember {
            mutableStateOf(
                TextPaint().apply {
                    color = noteColor.toArgb()
                    isAntiAlias = true
                    textSize = noteTextSizePx
                }
            )
        }

        val context = LocalContext.current
        val displayMetrics = context.resources.displayMetrics
        LaunchedEffect(numberTextSize, noteTextSize) {
            numberTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                numberTextSize.value,
                displayMetrics
            )
            noteTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                noteTextSize.value,
                displayMetrics
            )
            numberPaint = TextPaint().apply {
                color = numberColor.toArgb()
                isAntiAlias = true
                textSize = numberTextSizePx
            }
            selectedNumberPaint = TextPaint().apply {
                color = selectedNumberColor.toArgb()
                isAntiAlias = true
                textSize = numberTextSizePx
            }
            notePaint = TextPaint().apply {
                color = noteColor.toArgb()
                isAntiAlias = true
                textSize = noteTextSizePx
            }
            errorNumberPaint = TextPaint().apply {
                color = errorNumberColor.toArgb()
                isAntiAlias = true
                textSize = numberTextSizePx
            }
            lockedNumberPaint = TextPaint().apply {
                color = lockedNumberColor.toArgb()
                isAntiAlias = true
                textSize = numberTextSizePx
            }
        }

        val boardModifier = Modifier
            .fillMaxSize()
            .pointerInput(key1 = isEnabled, key2 = board) {
                detectTapGestures(
                    onTap = {
                        if (isEnabled) {
                            val totalOffset = it / zoom + offset
                            val row = floor((totalOffset.y) / cellSizePx)
                                .toInt()
                                .coerceIn(board.indices)
                            val column = floor((totalOffset.x) / cellSizePx)
                                .toInt()
                                .coerceIn(board.indices)
                            onSelectCell(board[row][column])
                        }
                    }
                )
            }

        val zoomModifier = Modifier
            .pointerInput(isEnabled) {
                detectTransformGestures(
                    onGesture = { gestureCentroid, gesturePan, gestureZoom, _ ->
                        if (isEnabled) {
                            val oldScale = zoom

                            val newScale = (zoom * gestureZoom).coerceIn(MinZoomRange..MaxZoomRange)

                            offset = (offset + gestureCentroid / oldScale) -
                                (gestureCentroid / newScale + gesturePan / oldScale)

                            zoom = newScale
                            if (offset.x < 0) {
                                offset = Offset(0f, offset.y)
                            } else if (maxSize - offset.x < maxSize / zoom) {
                                offset = offset.copy(x = maxSize - maxSize / zoom)
                            }
                            if (offset.y < 0) {
                                offset = Offset(offset.x, 0f)
                            } else if (maxSize - offset.y < maxSize / zoom) {
                                offset = offset.copy(y = maxSize - maxSize / zoom)
                            }
                        }
                    }
                )
            }
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom
                scaleY = zoom
                TransformOrigin(0f, 0f).also { transformOrigin = it }
            }

        Canvas(
            modifier = if (isZoomable) {
                boardModifier.then(zoomModifier)
            } else {
                boardModifier
            }
        ) {
            fillAllCellsWithBackground(
                board = board,
                size = size,
                cornerRadius = cornerRadius,
                cellColor = cellColor,
                cellSize = cellSize,
                cellSizePx = cellSizePx
            )

            val hasSelectedCell = selectedCell.row >= 0 && selectedCell.col >= 0
            if (hasSelectedCell) {
                drawSelectedCell(
                    selectedCell = selectedCell,
                    cornerRadius = cornerRadius,
                    selectedCellColor = selectedCellColor,
                    cellSize = cellSize,
                    isPositionCells = isPositionCells,
                    cellSizePx = cellSizePx,
                    size = size
                )
            }

            if (isIdenticalNumbersHighlight) {
                fillIdenticalCells(
                    board = board,
                    size = size,
                    selectedCell = selectedCell,
                    selectedCellColor = selectedCellColor,
                    cornerRadius = cornerRadius,
                    cellSize = cellSize,
                    cellSizePx = cellSizePx
                )
            }

            drawHorizontalLines(
                boardSize = size,
                cellSize = cellSizePx,
                innerStrokeThickness = horizontalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxSize,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawVerticalLines(
                boardSize = size,
                cellSize = cellSizePx,
                innerStrokeThickness = verticalInnerStrokeThickness,
                outerStrokeColor = outerStrokeColor,
                outerStrokeWidth = outerStrokeWidthPx,
                maxWidth = maxSize,
                innerStrokeWidth = innerStrokeWidthPx,
                innerStrokeColor = innerStrokeColor
            )

            drawNumbers(
                board = board,
                boardSize = size,
                isErrorsHighlight = isErrorsHighlight,
                errorNumberPaint = errorNumberPaint,
                lockedNumberPaint = lockedNumberPaint,
                selectedNumberPaint = selectedNumberPaint,
                selectedCell = selectedCell,
                isIdenticalNumberHighlight = isIdenticalNumbersHighlight,
                numberPaint = numberPaint,
                cellSize = cellSizePx
            )

            val canDrawNotes = notes.isNotEmpty() && isRenderNotes
            if (canDrawNotes) {
                drawNotes(
                    gameType = gameType,
                    paint = notePaint,
                    notes = notes,
                    cellSize = cellSizePx,
                    cellSizeDividerWidth = cellSizeDividerWidth,
                    cellSizeDividerHeight = cellSizeDividerHeight
                )
            }
        }
    }
}

private fun DrawScope.fillIdenticalCells(
    board: ImmutableBoardList,
    size: Int,
    selectedCell: BoardCell,
    selectedCellColor: Color,
    cornerRadius: CornerRadius,
    cellSize: Size,
    cellSizePx: Float,
) {
    for (i in 0 until size) {
        for (j in 0 until size) {
            val cell = board[i][j]
            if (cell.value == selectedCell.value && cell.value != 0) {
                drawCell(
                    cornerRadius = cornerRadius,
                    color = selectedCellColor.copy(alpha = 0.6f),
                    boardSize = size,
                    cellSize = cellSize,
                    cellOffset = Offset(
                        x = cell.col * cellSizePx,
                        y = cell.row * cellSizePx
                    )
                )
            }
        }
    }
}

private fun DrawScope.drawSelectedCell(
    selectedCell: BoardCell,
    cornerRadius: CornerRadius,
    selectedCellColor: Color,
    cellSize: Size,
    isPositionCells: Boolean,
    cellSizePx: Float,
    size: Int,
) {
    val selectedOffset = Offset(
        x = selectedCell.col * cellSizePx,
        y = selectedCell.row * cellSizePx
    )

    drawCell(
        cornerRadius = cornerRadius,
        color = selectedCellColor,
        boardSize = size,
        cellSize = cellSize,
        cellOffset = selectedOffset
    )
    if (isPositionCells) {
        drawVerticalPositionCells(
            cornerRadius = cornerRadius,
            color = selectedCellColor.copy(alpha = 0.2f),
            boardSize = size,
            cellSize = cellSize,
            cellOffset = selectedOffset
        )
        drawHorizontalPositionCells(
            cornerRadius = cornerRadius,
            color = selectedCellColor.copy(alpha = 0.2f),
            boardSize = size,
            cellSize = cellSize,
            cellOffset = selectedOffset
        )
    }
}

private fun DrawScope.fillAllCellsWithBackground(
    board: ImmutableBoardList,
    size: Int,
    cornerRadius: CornerRadius,
    cellColor: Color,
    cellSize: Size,
    cellSizePx: Float,
) {
    for (i in 0 until size) {
        for (j in 0 until size) {
            val cell = board[i][j]
            drawCell(
                cornerRadius = cornerRadius,
                color = cellColor,
                boardSize = size,
                cellSize = cellSize,
                cellOffset = Offset(
                    x = cell.col * cellSizePx,
                    y = cell.row * cellSizePx
                )
            )
        }
    }
}

private fun DrawScope.drawVerticalPositionCells(
    cornerRadius: CornerRadius,
    color: Color,
    boardSize: Int,
    cellSize: Size,
    cellOffset: Offset,
) {
    for (j in 0 until boardSize) {
        drawCell(
            cornerRadius = cornerRadius,
            color = color,
            boardSize = boardSize,
            cellSize = cellSize,
            cellOffset = Offset(
                x = cellOffset.x,
                y = j * cellSize.height
            )
        )
    }
}

private fun DrawScope.drawHorizontalPositionCells(
    cornerRadius: CornerRadius,
    color: Color,
    boardSize: Int,
    cellSize: Size,
    cellOffset: Offset,
) {
    for (i in 0 until boardSize) {
        drawCell(
            cornerRadius = cornerRadius,
            color = color,
            boardSize = boardSize,
            cellSize = cellSize,
            cellOffset = Offset(
                x = i * cellSize.width,
                y = cellOffset.y
            )
        )
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

private fun DrawScope.drawNumbers(
    board: ImmutableBoardList,
    boardSize: Int,
    isErrorsHighlight: Boolean,
    errorNumberPaint: TextPaint,
    lockedNumberPaint: TextPaint,
    numberPaint: TextPaint,
    selectedNumberPaint: TextPaint,
    selectedCell: BoardCell,
    isIdenticalNumberHighlight: Boolean,
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
                    isErrorsHighlight = isErrorsHighlight,
                    errorNumberPaint = errorNumberPaint,
                    lockedNumberPaint = lockedNumberPaint,
                    numberPaint = numberPaint,
                    selectedNumberPaint = selectedNumberPaint,
                    selectedCell = selectedCell,
                    isIdenticalNumberHighlight = isIdenticalNumberHighlight,
                    cellSize = cellSize
                )
            }
        }
    }
}

private fun DrawScope.drawNumber(
    cell: BoardCell,
    number: Int,
    isErrorsHighlight: Boolean,
    errorNumberPaint: TextPaint,
    lockedNumberPaint: TextPaint,
    numberPaint: TextPaint,
    selectedNumberPaint: TextPaint,
    selectedCell: BoardCell,
    isIdenticalNumberHighlight: Boolean,
    cellSize: Float,
) {
    val paint = when {
        cell.isError && isErrorsHighlight -> errorNumberPaint
        cell.isSelected(selectedCell, isIdenticalNumberHighlight) -> selectedNumberPaint
        cell.isLocked -> lockedNumberPaint
        else -> numberPaint
    }

    val textToDraw = number.toString(BoardRadix).uppercase()
    val textBounds = android.graphics.Rect()
    paint.getTextBounds(textToDraw, 0, 1, textBounds)
    val textWidth = paint.measureText(textToDraw)
    val textPosX = cell.col * cellSize + (cellSize - textWidth) / 2f
    val textPosY = cell.row * cellSize + (cellSize + textBounds.height()) / 2f

    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, paint)
    }
}

private fun DrawScope.drawNotes(
    gameType: GameType,
    paint: TextPaint,
    notes: List<BoardNote>,
    cellSize: Float,
    cellSizeDividerWidth: Float,
    cellSizeDividerHeight: Float,
) {
    val noteBounds = android.graphics.Rect()
    paint.getTextBounds("1", 0, 1, noteBounds)

    drawIntoCanvas { canvas ->
        notes.forEach { note ->
            val textToDraw = note.value.toString(BoardRadix).uppercase()
            val noteTextMeasure = paint.measureText(textToDraw)
            val noteRowNumber = getNoteRowNumber(note.value, gameType)
            val textPosX = note.col * cellSize + cellSizeDividerWidth / 2f +
                (cellSizeDividerWidth * noteRowNumber) - noteTextMeasure / 2f
            val noteColumnNumber = getNoteColumnNumber(note.value, gameType)
            val textPosY = note.row * cellSize + cellSizeDividerHeight / 2f +
                (cellSizeDividerHeight * noteColumnNumber) + noteBounds.height() / 2f
            canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, paint)
        }
    }
}

private fun BoardCell.isSelected(
    selectedCell: BoardCell,
    isIdenticalNumberHighlight: Boolean,
): Boolean {
    return (isIdenticalNumberHighlight && value == selectedCell.value) ||
        (selectedCell.row == row && selectedCell.col == col)
}

@Suppress("MagicNumber")
private fun getNoteColumnNumber(
    number: Int,
    gameType: GameType,
): Int = when (gameType) {
    GameType.DEFAULT9X9, GameType.DEFAULT6X6 -> when (number) {
        1, 2, 3 -> 0
        4, 5, 6 -> 1
        7, 8, 9 -> 2
        else -> 0
    }

    GameType.DEFAULT12X12 -> when (number) {
        1, 2, 3, 4 -> 0
        5, 6, 7, 8 -> 1
        9, 10, 11, 12 -> 2
        else -> 0
    }

    else -> 0
}

@Suppress("MagicNumber")
private fun getNoteRowNumber(
    number: Int,
    gameType: GameType,
): Int = when (gameType) {
    GameType.DEFAULT9X9, GameType.DEFAULT6X6 -> when (number) {
        1, 4, 7 -> 0
        2, 5, 8 -> 1
        3, 6, 9 -> 2
        else -> 0
    }

    GameType.DEFAULT12X12 -> when (number) {
        1, 5, 9 -> 0
        2, 6, 10 -> 1
        3, 7, 11 -> 2
        4, 8, 12 -> 3
        else -> 0
    }

    else -> 0
}

@Composable
private fun Modifier.boardBackground(): Modifier = this.then(
    Modifier
        .background(
            color = SudokuTheme.colors.onPrimary,
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
)

@Preview(
    name = "Game Board Preview",
)
@Composable
private fun GameBoardPreview() {
    @Suppress("MagicNumber")
    val boardSize = 9

    SudokuTheme {
        GameBoard(
            board = List(boardSize) { row ->
                List(boardSize) { col ->
                    BoardCell(
                        row = row,
                        col = col,
                        value = Random.nextInt(boardSize)
                    )
                }
            }.toImmutable(),
            selectedCell = BoardCell(
                row = 3,
                col = 2,
                value = 3
            ),
            onSelectCell = {},
            notes = listOf(
                BoardNote(
                    row = 2,
                    col = 2,
                    value = 5
                ),
            )
        )
    }
}
