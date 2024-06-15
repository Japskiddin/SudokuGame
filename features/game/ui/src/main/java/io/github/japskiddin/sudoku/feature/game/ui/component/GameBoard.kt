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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.model.BoardNote
import io.github.japskiddin.sudoku.core.game.qqwing.GameDifficulty
import io.github.japskiddin.sudoku.core.game.qqwing.GameType
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.ui.component.innerShadow
import io.github.japskiddin.sudoku.core.ui.theme.BoardCellNormal
import io.github.japskiddin.sudoku.core.ui.theme.BoardCellSelected
import io.github.japskiddin.sudoku.core.ui.theme.BoardNumberNormal
import io.github.japskiddin.sudoku.core.ui.theme.BoardNumberSelected
import io.github.japskiddin.sudoku.core.ui.theme.OnPrimary
import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.feature.game.domain.GameState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
internal fun GameBoard(
    modifier: Modifier = Modifier,
    board: List<List<BoardCell>>,
    @Suppress("MagicNumber")
    gameType: GameType = when (board.size) {
        6 -> GameType.DEFAULT6X6
        9 -> GameType.DEFAULT9X9
        12 -> GameType.DEFAULT12X12
        else -> GameType.UNSPECIFIED
    },
    selectedCell: BoardCell,
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
    isQuestions: Boolean = false,
    isRenderNotes: Boolean = true,
    isZoomable: Boolean = false,
    isDrawBoardFrame: Boolean = false,
    isCrossHighlight: Boolean = false,
    cellsToHighlight: List<BoardCell> = emptyList(),
    notes: List<BoardNote> = emptyList(),
    numberColor: Color = BoardNumberNormal,
    selectedNumberColor: Color = BoardNumberSelected,
    lockedNumberColor: Color = Color.Black,
    errorNumberColor: Color = Color.Red,
    cellColor: Color = BoardCellNormal,
    selectedCellColor: Color = BoardCellSelected,
    noteColor: Color = numberColor.copy(alpha = 0.8f),
    outerStrokeColor: Color = Color.Black,
    innerStrokeColor: Color = outerStrokeColor.copy(alpha = 0.2f),
    onSelectCell: (BoardCell) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = OnPrimary,
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(8.dp)
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
    ) {
        val maxWidth = constraints.maxWidth.toFloat()

        val boardSize = gameType.size

        val cellSizePx by remember(boardSize) { mutableFloatStateOf(maxWidth / boardSize.toFloat()) }
        val cellSizeDividerWidth by remember(boardSize) {
            mutableFloatStateOf(
                cellSizePx /
                    ceil(
                        sqrt(
                            boardSize.toFloat()
                        )
                    )
            )
        }
        val cellSizeDividerHeight by remember(boardSize) {
            mutableFloatStateOf(
                cellSizePx /
                    floor(
                        sqrt(
                            boardSize.toFloat()
                        )
                    )
            )
        }

        val verticalInnerStrokeThickness by remember(
            boardSize
        ) { mutableIntStateOf(floor(sqrt(boardSize.toFloat())).toInt()) }
        val horizontalInnerStrokeThickness by remember(boardSize) {
            mutableIntStateOf(
                ceil(
                    sqrt(
                        boardSize.toFloat()
                    )
                ).toInt()
            )
        }

        var numberTextSizePx = with(LocalDensity.current) { numberTextSize.toPx() }
        var noteTextSizePx = with(LocalDensity.current) { noteTextSize.toPx() }

        val outerCornerRadiusPx: Float = with(LocalDensity.current) { outerCornerRadius.toPx() }
        val outerStrokeWidthPx = with(LocalDensity.current) { outerStrokeWidth.toPx() }
        val innerStrokeWidthPx: Float = with(LocalDensity.current) { innerStrokeWidth.toPx() }
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
        LaunchedEffect(numberTextSize, noteTextSize) {
            numberTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                numberTextSize.value,
                context.resources.displayMetrics
            )
            noteTextSizePx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                noteTextSize.value,
                context.resources.displayMetrics
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

                            @Suppress("MagicNumber")
                            val minRange = 1f

                            @Suppress("MagicNumber")
                            val maxRange = 3f

                            val newScale = (zoom * gestureZoom).coerceIn(minRange..maxRange)

                            offset = (offset + gestureCentroid / oldScale) -
                                (gestureCentroid / newScale + gesturePan / oldScale)

                            zoom = newScale
                            if (offset.x < 0) {
                                offset = Offset(0f, offset.y)
                            } else if (maxWidth - offset.x < maxWidth / zoom) {
                                offset = offset.copy(x = maxWidth - maxWidth / zoom)
                            }
                            if (offset.y < 0) {
                                offset = Offset(offset.x, 0f)
                            } else if (maxWidth - offset.y < maxWidth / zoom) {
                                offset = offset.copy(y = maxWidth - maxWidth / zoom)
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
            modifier = if (isZoomable) boardModifier.then(zoomModifier) else boardModifier
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

            // закрашиваем выделенную клетку, если есть
            if (selectedCell.row >= 0 && selectedCell.col >= 0) {
                val selectedOffset = Offset(
                    x = selectedCell.col * cellSizePx,
                    y = selectedCell.row * cellSizePx
                )

                drawCell(
                    cornerRadius = cornerRadius,
                    color = selectedCellColor,
                    boardSize = boardSize,
                    cellSize = cellSize,
                    cellOffset = selectedOffset
                )
                if (isPositionCells) {
                    drawVerticalPositionCells(
                        cornerRadius = cornerRadius,
                        color = selectedCellColor.copy(alpha = 0.2f),
                        boardSize = boardSize,
                        cellSize = cellSize,
                        cellOffset = selectedOffset
                    )
                    drawHorizontalPositionCells(
                        cornerRadius = cornerRadius,
                        color = selectedCellColor.copy(alpha = 0.2f),
                        boardSize = boardSize,
                        cellSize = cellSize,
                        cellOffset = selectedOffset
                    )
                }
            }

            // закрашиваем клетки с таким же значением
            if (isIdenticalNumbersHighlight) {
                for (i in 0 until boardSize) {
                    for (j in 0 until boardSize) {
                        val cell = board[i][j]
                        if (cell.value == selectedCell.value && cell.value != 0) {
                            drawCell(
                                cornerRadius = cornerRadius,
                                color = selectedCellColor,
                                boardSize = boardSize,
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

            cellsToHighlight.forEach {
                drawCell(
                    cornerRadius = cornerRadius,
                    color = selectedCellColor.copy(alpha = 0.5f),
                    boardSize = boardSize,
                    cellSize = cellSize,
                    cellOffset = Offset(
                        x = it.col * cellSizePx,
                        y = it.row * cellSizePx
                    )
                )
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
                isErrorsHighlight = isErrorsHighlight,
                errorNumberPaint = errorNumberPaint,
                lockedNumberPaint = lockedNumberPaint,
                selectedNumberPaint = selectedNumberPaint,
                selectedCell = selectedCell,
                isIdenticalNumberHighlight = isIdenticalNumbersHighlight,
                numberPaint = numberPaint,
                isQuestions = isQuestions,
                cellSize = cellSizePx
            )

            if (notes.isNotEmpty() && !isQuestions && isRenderNotes) {
                drawNotes(
                    gameType = gameType,
                    paint = notePaint,
                    notes = notes,
                    cellSize = cellSizePx,
                    cellSizeDividerWidth = cellSizeDividerWidth,
                    cellSizeDividerHeight = cellSizeDividerHeight
                )
            }

            if (isCrossHighlight) {
                val sectionHeight = gameType.sectionHeight
                val sectionWidth = gameType.sectionWidth
                for (i in 0 until boardSize / sectionWidth) {
                    for (j in 0 until boardSize / sectionHeight) {
                        @Suppress("ComplexCondition")
                        if ((i % 2 == 0 && j % 2 != 0) ||
                            (i % 2 != 0 && j % 2 == 0)
                        ) {
                            drawRect(
                                color = selectedCellColor.copy(alpha = 0.1f),
                                topLeft = Offset(
                                    x = i * sectionWidth * cellSizePx,
                                    y = j * sectionHeight * cellSizePx
                                ),
                                size = Size(cellSizePx * sectionWidth, cellSizePx * sectionHeight)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawBoardFrame(
    outerStrokeColor: Color,
    outerStrokeWidth: Float,
    maxWidth: Float,
    cornerRadius: CornerRadius
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
    innerStrokeColor: Color
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
    innerStrokeColor: Color
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

private fun DrawScope.drawVerticalPositionCells(
    cornerRadius: CornerRadius,
    color: Color,
    boardSize: Int,
    cellSize: Size,
    cellOffset: Offset
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
    cellOffset: Offset
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
    cellOffset: Offset
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
    color: Color
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
    bottomRightCorner: CornerRadius = CornerRadius.Zero
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
    board: List<List<BoardCell>>,
    isErrorsHighlight: Boolean,
    errorNumberPaint: TextPaint,
    lockedNumberPaint: TextPaint,
    numberPaint: TextPaint,
    selectedNumberPaint: TextPaint,
    selectedCell: BoardCell,
    isQuestions: Boolean,
    isIdenticalNumberHighlight: Boolean,
    cellSize: Float
) {
    drawIntoCanvas { canvas ->
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val number = board[i][j]
                if (number.value != 0) {
                    val paint = when {
                        number.isError && isErrorsHighlight -> errorNumberPaint
                        number.isLocked -> lockedNumberPaint
                        (
                            (selectedCell.row >= 0 && selectedCell.col >= 0) &&
                                (
                                    (isIdenticalNumberHighlight && number.value == selectedCell.value) ||
                                        (selectedCell.row == i && selectedCell.col == j)
                                    )
                            ) -> selectedNumberPaint

                        else -> numberPaint
                    }

                    @Suppress("MagicNumber")
                    val radix = 16
                    val textToDraw = if (isQuestions) "?" else number.value.toString(radix).uppercase()
                    val textBounds = android.graphics.Rect()
                    numberPaint.getTextBounds(textToDraw, 0, 1, textBounds)
                    val textWidth = paint.measureText(textToDraw)
                    val textPosX = number.col * cellSize + (cellSize - textWidth) / 2f
                    val textPosY = number.row * cellSize + (cellSize + textBounds.height()) / 2f
                    canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, paint)
                }
            }
        }
    }
}

private fun DrawScope.drawNotes(
    gameType: GameType,
    paint: TextPaint,
    notes: List<BoardNote>,
    cellSize: Float,
    cellSizeDividerWidth: Float,
    cellSizeDividerHeight: Float
) {
    val noteBounds = android.graphics.Rect()
    paint.getTextBounds("1", 0, 1, noteBounds)

    @Suppress("MagicNumber")
    val radix = 16
    drawIntoCanvas { canvas ->
        notes.forEach { note ->
            val textToDraw = note.value.toString(radix).uppercase()
            val noteTextMeasure = paint.measureText(textToDraw)
            val textPosX =
                note.col * cellSize + cellSizeDividerWidth / 2f + (
                    cellSizeDividerWidth *
                        getNoteRowNumber(
                            note.value,
                            gameType
                        )
                    ) - noteTextMeasure / 2f
            val textPosY =
                note.row * cellSize + cellSizeDividerHeight / 2f + (
                    cellSizeDividerHeight *
                        getNoteColumnNumber(
                            note.value,
                            gameType
                        )
                    ) + noteBounds.height() / 2f
            canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, paint)
        }
    }
}

@Suppress("MagicNumber")
private fun getNoteColumnNumber(
    number: Int,
    gameType: GameType
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
    gameType: GameType
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

@Preview(
    name = "Game Board Preview"
)
@Composable
private fun GameBoardPreview(
    @PreviewParameter(GameBoardUiPreviewProvider::class) state: GameState
) {
    val notes: List<BoardNote> = listOf(BoardNote(row = 2, col = 2, value = 5))
    GameBoard(
        board = state.board,
        selectedCell = state.selectedCell,
        onSelectCell = {},
        notes = notes
    )
}

internal class GameBoardUiPreviewProvider : PreviewParameterProvider<GameState> {
    private val parser = SudokuParser()
    private val board = Board(
        initialBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        solvedBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        difficulty = GameDifficulty.INTERMEDIATE,
        type = GameType.DEFAULT9X9
    )

    override val values: Sequence<GameState>
        get() = sequenceOf(
            GameState(
                board = parser.parseBoard(
                    board = board.initialBoard,
                    gameType = board.type
                )
                    .map { item -> item.toImmutableList() }
                    .toImmutableList(),
                notes = persistentListOf(),
                selectedCell = BoardCell(
                    row = 3,
                    col = 2,
                    value = 3
                )
            )
        )
}
