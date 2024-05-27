package io.github.japskiddin.sudoku.feature.component

import android.text.TextPaint
import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.feature.game.GameState
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@Composable
internal fun GameBoard(
  modifier: Modifier = Modifier,
  board: List<List<BoardCell>>,
  size: Int = board.size,
  selectedCell: BoardCell,
  outerCornerRadius: Dp = 12.dp,
  outerStrokeWidth: Dp = 1.5.dp,
  innerStrokeWidth: Dp = 1.dp,
  numberTextSize: TextUnit = when (size) {
    6 -> 32.sp
    9 -> 26.sp
    12 -> 24.sp
    else -> 14.sp
  },
  noteTextSize: TextUnit = when (size) {
    6 -> 18.sp
    9 -> 12.sp
    12 -> 7.sp
    else -> 14.sp
  },
  isIdenticalNumbersHighlight: Boolean = true,
  isErrorsHighlight: Boolean = true,
  isPositionLines: Boolean = true,
  isEnabled: Boolean = true,
  isQuestions: Boolean = false,
  isRenderNotes: Boolean = true,
  isZoomable: Boolean = false,
  isCrossHighlight: Boolean = false,
  cellsToHighlight: List<BoardCell>? = null,
  notes: List<BoardNote>? = null,
  numberColor: Color = Color.Black,
  noteColor: Color = Color.Black,
  lockedNumberColor: Color = Color.Black,
  errorNumberColor: Color = Color.Red,
  selectedColor: Color = Color.Green,
  backgroundColor: Color = Color.White,
  outerStrokeColor: Color = Color.Black,
  innerStrokeColor: Color = outerStrokeColor.copy(alpha = 0.2f),
  onSelectCell: (BoardCell) -> Unit,
) {
  BoxWithConstraints(
    modifier = modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .padding(4.dp)
  ) {
    val maxWidth = constraints.maxWidth.toFloat()

    val cellSize by remember(size) { mutableFloatStateOf(maxWidth / size.toFloat()) }
    val cellSizeDividerWidth by remember(size) { mutableFloatStateOf(cellSize / ceil(sqrt(size.toFloat()))) }
    val cellSizeDividerHeight by remember(size) { mutableFloatStateOf(cellSize / floor(sqrt(size.toFloat()))) }

    val verticalInnerStrokeThickness by remember(size) { mutableIntStateOf(floor(sqrt(size.toFloat())).toInt()) }
    val horizontalInnerStrokeThickness by remember(size) { mutableIntStateOf(ceil(sqrt(size.toFloat())).toInt()) }

    var numberTextSizePx = with(LocalDensity.current) { numberTextSize.toPx() }
    var noteTextSizePx = with(LocalDensity.current) { noteTextSize.toPx() }

    val outerCornerRadiusPx: Float = with(LocalDensity.current) { outerCornerRadius.toPx() }
    val outerStrokeWidthPx = with(LocalDensity.current) { outerStrokeWidth.toPx() }
    val innerStrokeWidthPx: Float = with(LocalDensity.current) { innerStrokeWidth.toPx() }
    val cornerRadius = CornerRadius(outerCornerRadiusPx, outerCornerRadiusPx)

    var zoom by remember(isEnabled) { mutableFloatStateOf(1f) }
    var offset by remember(isEnabled) { mutableStateOf(Offset.Zero) }

    var numberPaint by remember {
      mutableStateOf(
        TextPaint().apply {
          color = numberColor.toArgb()
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
              val row = floor((totalOffset.y) / cellSize)
                .toInt()
                .coerceIn(board.indices)
              val column = floor((totalOffset.x) / cellSize)
                .toInt()
                .coerceIn(board.indices)
              onSelectCell(board[row][column])
            }
          },
        )
      }

    val zoomModifier = Modifier
      .pointerInput(isEnabled) {
        detectTransformGestures(
          onGesture = { gestureCentroid, gesturePan, gestureZoom, _ ->
            if (isEnabled) {
              val oldScale = zoom
              val newScale = (zoom * gestureZoom).coerceIn(1f..3f)

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
      for (i in 0 until size) {
        for (j in 0 until size) {
          val itemOffset = Offset(
            x = board[i][j].col * cellSize,
            y = board[i][j].row * cellSize
          )
          val itemSize = Size(cellSize, cellSize)
          if (i == 0 && j == 0) {
            drawRoundCell(
              offset = itemOffset,
              size = itemSize,
              color = backgroundColor,
              topLeftCorner = cornerRadius,
            )
          } else if (i == 0 && j == size - 1) {
            drawRoundCell(
              offset = itemOffset,
              size = itemSize,
              color = backgroundColor,
              topRightCorner = cornerRadius,
            )
          } else if (i == size - 1 && j == 0) {
            drawRoundCell(
              offset = itemOffset,
              size = itemSize,
              color = backgroundColor,
              bottomLeftCorner = cornerRadius,
            )
          } else if (i == size - 1 && j == size - 1) {
            drawRoundCell(
              offset = itemOffset,
              size = itemSize,
              color = backgroundColor,
              bottomRightCorner = cornerRadius,
            )
          } else {
            drawCell(
              offset = itemOffset,
              size = itemSize,
              color = backgroundColor,
            )
          }
        }
      }

      // закрашиваем выделенную клетку, если есть
      if (selectedCell.row >= 0 && selectedCell.col >= 0) {
        drawCell(
          color = selectedColor,
          offset = Offset(
            x = selectedCell.col * cellSize,
            y = selectedCell.row * cellSize
          ),
          size = Size(cellSize, cellSize),
        )
        if (isPositionLines) {
          drawCell(
            color = selectedColor.copy(alpha = 0.2f),
            offset = Offset(
              x = selectedCell.col * cellSize,
              y = 0f
            ),
            size = Size(cellSize, maxWidth),
          )
          drawCell(
            color = selectedColor.copy(alpha = 0.2f),
            offset = Offset(
              x = 0f,
              y = selectedCell.row * cellSize
            ),
            size = Size(maxWidth, cellSize),
          )
        }
      }

      // закрашиваем клетки с таким же значением
      if (isIdenticalNumbersHighlight) {
        for (i in 0 until size) {
          for (j in 0 until size) {
            if (board[i][j].value == selectedCell.value && board[i][j].value != 0) {
              drawCell(
                color = selectedColor,
                offset = Offset(
                  x = board[i][j].col * cellSize,
                  y = board[i][j].row * cellSize
                ),
                size = Size(cellSize, cellSize),
              )
            }
          }
        }
      }

      cellsToHighlight?.forEach {
        drawCell(
          color = selectedColor.copy(alpha = 0.5f),
          offset = Offset(
            x = it.col * cellSize,
            y = it.row * cellSize
          ),
          size = Size(cellSize, cellSize),
        )
      }

      drawBoardFrame(
        outerStrokeColor = outerStrokeColor,
        outerStrokeWidth = outerStrokeWidthPx,
        maxWidth = maxWidth,
        cornerRadius = cornerRadius,
      )

      drawHorizontalLines(
        size = size,
        cellSize = cellSize,
        innerStrokeThickness = horizontalInnerStrokeThickness,
        outerStrokeColor = outerStrokeColor,
        outerStrokeWidth = outerStrokeWidthPx,
        maxWidth = maxWidth,
        innerStrokeWidth = innerStrokeWidthPx,
        innerStrokeColor = innerStrokeColor,
      )

      drawVerticalLines(
        size = size,
        cellSize = cellSize,
        innerStrokeThickness = verticalInnerStrokeThickness,
        outerStrokeColor = outerStrokeColor,
        outerStrokeWidth = outerStrokeWidthPx,
        maxWidth = maxWidth,
        innerStrokeWidth = innerStrokeWidthPx,
        innerStrokeColor = innerStrokeColor,
      )

      drawNumbers(
        size = size,
        board = board,
        isErrorsHighlight = isErrorsHighlight,
        errorNumberPaint = errorNumberPaint,
        lockedNumberPaint = lockedNumberPaint,
        numberPaint = numberPaint,
        isQuestions = isQuestions,
        cellSize = cellSize,
      )

      if (!notes.isNullOrEmpty() && !isQuestions && isRenderNotes) {
        drawNotes(
          size = size,
          paint = notePaint,
          notes = notes,
          cellSize = cellSize,
          cellSizeDividerWidth = cellSizeDividerWidth,
          cellSizeDividerHeight = cellSizeDividerHeight,
        )
      }

      // doesn't look good on 6x6
      if (isCrossHighlight && size != 6) {
        val sectionHeight = getSectionHeightForSize(size)
        val sectionWidth = getSectionWidthForSize(size)
        for (i in 0 until size / sectionWidth) {
          for (j in 0 until size / sectionHeight) {
            if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) {
              drawRect(
                color = selectedColor.copy(alpha = 0.1f),
                topLeft = Offset(
                  x = i * sectionWidth * cellSize,
                  y = j * sectionHeight * cellSize
                ),
                size = Size(cellSize * sectionWidth, cellSize * sectionHeight)
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
  cornerRadius: CornerRadius,
) {
  drawRoundRect(
    color = outerStrokeColor,
    size = Size(maxWidth, maxWidth),
    cornerRadius = cornerRadius,
    style = Stroke(width = outerStrokeWidth),
  )
}

private fun DrawScope.drawHorizontalLines(
  size: Int,
  cellSize: Float,
  innerStrokeThickness: Int,
  outerStrokeColor: Color,
  outerStrokeWidth: Float,
  maxWidth: Float,
  innerStrokeWidth: Float,
  innerStrokeColor: Color,
) {
  for (i in 1 until size) {
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
  size: Int,
  cellSize: Float,
  innerStrokeThickness: Int,
  outerStrokeColor: Color,
  outerStrokeWidth: Float,
  maxWidth: Float,
  innerStrokeWidth: Float,
  innerStrokeColor: Color,
) {
  for (i in 1 until size) {
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

private fun DrawScope.drawRoundCell(
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
          size = size,
        ),
        topLeft = topLeftCorner,
        topRight = topRightCorner,
        bottomLeft = bottomLeftCorner,
        bottomRight = bottomRightCorner,
      )
    )
  }
  drawPath(path = path, color = color)
}

private fun DrawScope.drawNumbers(
  size: Int,
  board: List<List<BoardCell>>,
  isErrorsHighlight: Boolean,
  errorNumberPaint: TextPaint,
  lockedNumberPaint: TextPaint,
  numberPaint: TextPaint,
  isQuestions: Boolean,
  cellSize: Float,
) {
  drawIntoCanvas { canvas ->
    for (i in 0 until size) {
      for (j in 0 until size) {
        val number = board[i][j]
        if (number.value != 0) {
          val paint = when {
            number.isError && isErrorsHighlight -> errorNumberPaint
            number.isLocked -> lockedNumberPaint
            else -> numberPaint
          }

          val textToDraw = if (isQuestions) "?" else number.value.toString(16).uppercase()
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
  size: Int,
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
      val textToDraw = note.value.toString(16).uppercase()
      val noteTextMeasure = paint.measureText(textToDraw)
      val textPosX =
        note.col * cellSize + cellSizeDividerWidth / 2f + (cellSizeDividerWidth * getNoteRowNumber(
          note.value,
          size
        )) - noteTextMeasure / 2f
      val textPosY =
        note.row * cellSize + cellSizeDividerHeight / 2f + (cellSizeDividerHeight * getNoteColumnNumber(
          note.value,
          size
        )) + noteBounds.height() / 2f
      canvas.nativeCanvas.drawText(textToDraw, textPosX, textPosY, paint)
    }
  }
}

private fun getNoteColumnNumber(number: Int, size: Int): Int {
  if (size == 9 || size == 6) {
    return when (number) {
      1, 2, 3 -> 0
      4, 5, 6 -> 1
      7, 8, 9 -> 2
      else -> 0
    }
  } else if (size == 12) {
    return when (number) {
      1, 2, 3, 4 -> 0
      5, 6, 7, 8 -> 1
      9, 10, 11, 12 -> 2
      else -> 0
    }
  }
  return 0
}

private fun getNoteRowNumber(number: Int, size: Int): Int {
  if (size == 9 || size == 6) {
    return when (number) {
      1, 4, 7 -> 0
      2, 5, 8 -> 1
      3, 6, 9 -> 2
      else -> 0
    }
  } else if (size == 12) {
    return when (number) {
      1, 5, 9 -> 0
      2, 6, 10 -> 1
      3, 7, 11 -> 2
      4, 8, 12 -> 3
      else -> 0
    }
  }
  return 0
}

private fun getSectionHeightForSize(size: Int): Int {
  return when (size) {
    6 -> GameType.DEFAULT6X6.sectionHeight
    9 -> GameType.DEFAULT9X9.sectionHeight
    12 -> GameType.DEFAULT12X12.sectionHeight
    else -> GameType.DEFAULT9X9.sectionHeight
  }
}

private fun getSectionWidthForSize(size: Int): Int {
  return when (size) {
    6 -> GameType.DEFAULT6X6.sectionWidth
    9 -> GameType.DEFAULT9X9.sectionWidth
    12 -> GameType.DEFAULT12X12.sectionWidth
    else -> GameType.DEFAULT9X9.sectionWidth
  }
}

@Preview(
  name = "Game Board Preview",
)
@Composable
internal fun GameBoardPreview(
  @PreviewParameter(GameBoardUiPreviewProvider::class) state: GameState
) {
  val notes: List<BoardNote> = listOf(BoardNote(row = 2, col = 2, value = 5))
  GameBoard(
    board = state.board,
    selectedCell = state.selectedCell,
    onSelectCell = {},
    notes = notes,
  )
}

private class GameBoardUiPreviewProvider : PreviewParameterProvider<GameState> {
  val parser = SudokuParser()
  val board = Board(
    initialBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
    solvedBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
    difficulty = GameDifficulty.INTERMEDIATE,
    type = GameType.DEFAULT9X9,
  )

  override val values: Sequence<GameState>
    get() = sequenceOf(
      GameState(
        board = parser.parseBoard(
          board = board.initialBoard,
          gameType = board.type
        ).toList(),
        selectedCell = BoardCell(
          row = 3,
          col = 2,
          value = 3,
        ),
      ),
    )
}