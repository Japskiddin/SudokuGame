package io.github.japskiddin.sudoku.feature.component

import android.graphics.Rect
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.model.BoardNote
import io.github.japskiddin.sudoku.core.game.qqwing.GameType
import io.github.japskiddin.sudoku.feature.game.BuildConfig
import io.github.japskiddin.sudoku.feature.game.TAG
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

@Composable
internal fun GameBoard(
  modifier: Modifier = Modifier,
  board: List<List<BoardCell>>,
  selectedCell: BoardCell,
  boardCornerRadius: Float = with(LocalDensity.current) { 12.dp.toPx() },
  onSelectCell: (BoardCell) -> Unit,
  identicalNumbersHighlight: Boolean = true,
  isErrorsHighlight: Boolean = true,
  positionLines: Boolean = true,
  enabled: Boolean = true,
  questions: Boolean = false,
  renderNotes: Boolean = true,
  cellsToHighlight: List<BoardCell>? = null,
  zoomable: Boolean = false,
  notes: List<BoardNote>? = null,
  mainTextSize: TextUnit = when (board.size) {
    6 -> 32.sp
    9 -> 26.sp
    12 -> 24.sp
    else -> 14.sp
  },
  noteTextSize: TextUnit = when (board.size) {
    6 -> 18.sp
    9 -> 12.sp
    12 -> 7.sp
    else -> 14.sp
  },
  crossHighlight: Boolean = false,
) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing GameBoard")
  val foregroundColor: Color = Color.Black
  val notesColor: Color = Color.Black
  val altForegroundColor: Color = Color.Black
  val errorColor: Color = Color.Red
  val highlightColor: Color = Color.Green
  val boardStrokeColor: Color = Color.Black
  val thinLineColor: Color = Color.Black
  val size = board.size
  val divider = if (size >= 6) {
    size / 3
  } else {
    0
  }
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

    val vertThick by remember(size) { mutableIntStateOf(floor(sqrt(size.toFloat())).toInt()) }
    val horThick by remember(size) { mutableIntStateOf(ceil(sqrt(size.toFloat())).toInt()) }

    var fontSizePx = with(LocalDensity.current) { mainTextSize.toPx() }
    var noteSizePx = with(LocalDensity.current) { noteTextSize.toPx() }

    val thinLineWidth = with(LocalDensity.current) { 1.3.dp.toPx() }
    val boardStrokeWidth = with(LocalDensity.current) { 1.3.dp.toPx() }

    // paints
    // numbers
    var textPaint by remember {
      mutableStateOf(
        TextPaint().apply {
          color = foregroundColor.toArgb()
          isAntiAlias = true
          textSize = fontSizePx
        }
      )
    }
    // errors
    var errorTextPaint by remember {
      mutableStateOf(
        TextPaint().apply {
          color = errorColor.toArgb()
          isAntiAlias = true
          textSize = fontSizePx
        }
      )
    }
    // locked numbers
    var lockedTextPaint by remember {
      mutableStateOf(
        TextPaint().apply {
          color = altForegroundColor.toArgb()
          isAntiAlias = true
          textSize = fontSizePx
        }
      )
    }

    // notes
    var notePaint by remember {
      mutableStateOf(
        TextPaint().apply {
          color = notesColor.toArgb()
          isAntiAlias = true
          textSize = noteSizePx
        }
      )
    }

    val context = LocalContext.current
    LaunchedEffect(mainTextSize, noteTextSize) {
      fontSizePx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        mainTextSize.value,
        context.resources.displayMetrics
      )
      noteSizePx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        noteTextSize.value,
        context.resources.displayMetrics
      )
      textPaint = TextPaint().apply {
        color = foregroundColor.toArgb()
        isAntiAlias = true
        textSize = fontSizePx
      }
      notePaint = TextPaint().apply {
        color = notesColor.toArgb()
        isAntiAlias = true
        textSize = noteSizePx
      }
      errorTextPaint = TextPaint().apply {
        color = Color(230, 67, 83).toArgb()
        isAntiAlias = true
        textSize = fontSizePx
      }
      lockedTextPaint = TextPaint().apply {
        color = altForegroundColor.toArgb()
        isAntiAlias = true
        textSize = fontSizePx
      }
    }

    var zoom by remember(enabled) { mutableFloatStateOf(1f) }
    var offset by remember(enabled) { mutableStateOf(Offset.Zero) }

    val boardModifier = Modifier
      .fillMaxSize()
      .pointerInput(key1 = enabled, key2 = board) {
        detectTapGestures(
          onTap = {
            if (enabled) {
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
          // onLongPress = {
          //   if (enabled) {
          //     val totalOffset = it / zoom + offset
          //     val row = floor((totalOffset.y) / cellSize).toInt()
          //     val column = floor((totalOffset.x) / cellSize).toInt()
          //     onLongClick(board[row][column])
          //   }
          // }
        )
      }

    val zoomModifier = Modifier
      .pointerInput(enabled) {
        detectTransformGestures(
          onGesture = { gestureCentroid, gesturePan, gestureZoom, _ ->
            if (enabled) {
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
      modifier = if (zoomable) boardModifier.then(zoomModifier) else boardModifier
    ) {
      if (selectedCell.row >= 0 && selectedCell.col >= 0) {
        // current cell
        drawRect(
          color = highlightColor.copy(alpha = 0.2f),
          topLeft = Offset(
            x = selectedCell.col * cellSize,
            y = selectedCell.row * cellSize
          ),
          size = Size(cellSize, cellSize)
        )
        if (positionLines) {
          // vertical position line
          drawRect(
            color = highlightColor.copy(alpha = 0.1f),
            topLeft = Offset(
              x = selectedCell.col * cellSize,
              y = 0f
            ),
            size = Size(cellSize, maxWidth)
          )
          // horizontal position line
          drawRect(
            color = highlightColor.copy(alpha = 0.1f),
            topLeft = Offset(
              x = 0f,
              y = selectedCell.row * cellSize
            ),
            size = Size(maxWidth, cellSize)
          )
        }
      }
      if (identicalNumbersHighlight) {
        for (i in 0 until size) {
          for (j in 0 until size) {
            if (board[i][j].value == selectedCell.value && board[i][j].value != 0) {
              drawRect(
                color = highlightColor.copy(alpha = 0.2f),
                topLeft = Offset(
                  x = board[i][j].col * cellSize,
                  y = board[i][j].row * cellSize
                ),
                size = Size(cellSize, cellSize)
              )
            }
          }
        }
      }
      cellsToHighlight?.forEach {
        drawRect(
          color = highlightColor.copy(alpha = 0.3f),
          topLeft = Offset(
            x = it.col * cellSize,
            y = it.row * cellSize
          ),
          size = Size(cellSize, cellSize)
        )
      }

      drawBoardFrame(
        boardStrokeColor = boardStrokeColor,
        boardStrokeWidth = boardStrokeWidth,
        maxWidth = maxWidth,
        cornerRadius = CornerRadius(boardCornerRadius, boardCornerRadius)
      )

      drawHorizontalLines(
        size = size,
        cellSize = cellSize,
        lineThickness = horThick,
        boardStrokeColor = boardStrokeColor,
        boardStrokeWidth = boardStrokeWidth,
        maxWidth = maxWidth,
        thinLineWidth = thinLineWidth,
        thinLineColor = thinLineColor,
      )

      drawVerticalLines(
        size = size,
        cellSize = cellSize,
        lineThickness = vertThick,
        boardStrokeColor = boardStrokeColor,
        boardStrokeWidth = boardStrokeWidth,
        maxWidth = maxWidth,
        thinLineWidth = thinLineWidth,
        thinLineColor = thinLineColor,
      )

      drawNumbers(
        size = size,
        board = board,
        isErrorsHighlight = isErrorsHighlight,
        errorTextPaint = errorTextPaint,
        lockedTextPaint = lockedTextPaint,
        textPaint = textPaint,
        questions = questions,
        cellSize = cellSize,
      )

      if (!notes.isNullOrEmpty() && !questions && renderNotes) {
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
      if (crossHighlight && size != 6) {
        val sectionHeight = getSectionHeightForSize(size)
        val sectionWidth = getSectionWidthForSize(size)
        for (i in 0 until size / sectionWidth) {
          for (j in 0 until size / sectionHeight) {
            if ((i % 2 == 0 && j % 2 != 0) || (i % 2 != 0 && j % 2 == 0)) {
              drawRect(
                color = highlightColor.copy(alpha = 0.1f),
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

    // Column(modifier = Modifier.fillMaxWidth()) {
    //   for (i in board.indices) {
    //     Row(
    //       modifier = Modifier.height(IntrinsicSize.Min)
    //     ) {
    //       val cells = board[i]
    //       for (j in cells.indices) {
    //         Cell(
    //           boardCell = board[i][j],
    //           isSelected = false,
    //           isEditable = true,
    //           onClick = {
    //             onSelectCell(board[i][j])
    //           },
    //           modifier = Modifier
    //             .aspectRatio(1f)
    //             .weight(1f)
    //         )
    //         if (divider != 0 && ((j + 1) % divider == 0)) {
    //           VerticalDivider(
    //             color = Color.Black,
    //             thickness = 1.dp,
    //             modifier = Modifier.fillMaxHeight()
    //           )
    //         }
    //       }
    //     }
    //     if (divider != 0 && ((i + 1) % divider == 0)) {
    //       HorizontalDivider(
    //         color = Color.Black,
    //         thickness = 1.dp,
    //         modifier = Modifier
    //           .height(1.dp)
    //           .fillMaxWidth(),
    //       )
    //     }
    //   }
    // }
  }
}

private fun DrawScope.drawBoardFrame(
  boardStrokeColor: Color,
  boardStrokeWidth: Float,
  maxWidth: Float,
  cornerRadius: CornerRadius,
) {
  drawRoundRect(
    color = boardStrokeColor,
    size = Size(maxWidth, maxWidth),
    cornerRadius = cornerRadius,
    style = Stroke(width = boardStrokeWidth)
  )
}

private fun DrawScope.drawHorizontalLines(
  size: Int,
  cellSize: Float,
  lineThickness: Int,
  boardStrokeColor: Color,
  boardStrokeWidth: Float,
  maxWidth: Float,
  thinLineWidth: Float,
  thinLineColor: Color,
) {
  for (i in 1 until size) {
    val isThickLine = i % lineThickness == 0
    drawLine(
      color = if (isThickLine) boardStrokeColor else thinLineColor,
      start = Offset(cellSize * i.toFloat(), 0f),
      end = Offset(cellSize * i.toFloat(), maxWidth),
      strokeWidth = if (isThickLine) boardStrokeWidth else thinLineWidth
    )
  }
}

private fun DrawScope.drawVerticalLines(
  size: Int,
  cellSize: Float,
  lineThickness: Int,
  boardStrokeColor: Color,
  boardStrokeWidth: Float,
  maxWidth: Float,
  thinLineWidth: Float,
  thinLineColor: Color,
) {
  for (i in 1 until size) {
    val isThickLine = i % lineThickness == 0
    if (maxWidth >= cellSize * i) {
      drawLine(
        color = if (isThickLine) boardStrokeColor else thinLineColor,
        start = Offset(0f, cellSize * i.toFloat()),
        end = Offset(maxWidth, cellSize * i.toFloat()),
        strokeWidth = if (isThickLine) boardStrokeWidth else thinLineWidth
      )
    }
  }
}

private fun DrawScope.drawNumbers(
  size: Int,
  board: List<List<BoardCell>>,
  isErrorsHighlight: Boolean,
  errorTextPaint: TextPaint,
  lockedTextPaint: TextPaint,
  textPaint: TextPaint,
  questions: Boolean,
  cellSize: Float,
) {
  drawIntoCanvas { canvas ->
    for (i in 0 until size) {
      for (j in 0 until size) {
        val number = board[i][j]
        if (number.value != 0) {
          val paint = when {
            number.isError && isErrorsHighlight -> errorTextPaint
            number.isLocked -> lockedTextPaint
            else -> textPaint
          }

          val textToDraw = if (questions) "?" else number.value.toString(16).uppercase()
          val textBounds = Rect()
          textPaint.getTextBounds(textToDraw, 0, 1, textBounds)
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
  val noteBounds = Rect()
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

@Composable
internal fun Cell(
  boardCell: BoardCell,
  isSelected: Boolean,
  isEditable: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Cell")
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .background(
        color = if (isSelected) {
          Color.Green
        } else {
          Color.White
        }
      )
      .border(width = .1f.dp, color = Color.Black.copy(alpha = .7f))
      .then(modifier)
      .let {
        if (isEditable) {
          return@let it.clickable { onClick() }
        }
        it
      },
  ) {
    Text(
      text = if (boardCell.value == 0) {
        ""
      } else {
        boardCell.value.toString()
      },
      color = if (isSelected) {
        Color.White
      } else {
        Color.Black
      },
      modifier = Modifier.padding(4.dp),
    )
  }
}

@Preview(
  name = "Game Preview",
)
@Composable
internal fun GamePreview(
  // @PreviewParameter(GameLevelUiPreviewProvider::class) gameState: GameState
) {
  // Game(
  //   gameState = gameState,
  //   onInputCell = { _, _ -> },
  // )
}

@Preview(
  name = "Game Cell",
)
@Composable
internal fun CellPreview() {
  Row {
    Cell(
      boardCell = BoardCell(row = 0, col = 0, value = 1),
      isSelected = false,
      isEditable = true,
      onClick = {})
    Cell(
      boardCell = BoardCell(row = 0, col = 0, value = 2),
      isSelected = true,
      isEditable = true,
      onClick = {})
  }
}

// private class GameLevelUiPreviewProvider : PreviewParameterProvider<GameState> {
//   private val generator = SudokuGenerator(9, 50).apply {
//     generate()
//   }
//   private val currentBoard = generator.getResult().items
//   private val completedBoard = generator.getResult().completedItems
//
//   override val values: Sequence<GameState>
//     get() = sequenceOf(
//       GameState(
//         board = Board(),
//         savedGame = SavedGame(),
//       ),
//     )
// }