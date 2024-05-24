package io.github.japskiddin.sudoku.feature.game

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.feature.component.autosizetext.AutoSizeText
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

private const val TAG = "Game UI"

@Composable
fun GameScreen() {
  GameScreen(viewModel = hiltViewModel())
}

@Composable
internal fun GameScreen(viewModel: GameViewModel) {
  val state by viewModel.uiState.collectAsState()
  when (val currentState = state) {
    is UiState.Success -> Game(
      gameState = currentState.gameState,
      onSelectCell = { boardCell -> viewModel.onUpdateSelectedBoardCell(boardCell) },
      onInputCell = { cell, item -> viewModel.onInputCell(cell, item) }
    )

    is UiState.Error -> Error(message = currentState.message)
    is UiState.Loading -> Loading()
    UiState.None -> Empty()
  }
}

@Composable
internal fun Game(
  gameState: GameState,
  onSelectCell: (BoardCell) -> Unit,
  onInputCell: (Pair<Int, Int>, Int) -> Unit,
) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Game screen")

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
  ) {
    GameBoard(
      board = gameState.board,
      selectedCell = gameState.selectedCell,
      onSelectCell = { boardCell ->
        onSelectCell(boardCell)
      },
      modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()
    )
    // InputPanel(
    //   size = gameState.currentBoard.size,
    //   onClick = { item -> onInputCell(selectedCell.value, item) }
    // )
  }
}

@Composable
internal fun GameBoard(
  modifier: Modifier = Modifier,
  board: List<List<BoardCell>>,
  selectedCell: BoardCell,
  onSelectCell: (BoardCell) -> Unit,
) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing GameBoard")
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
      .border(
        width = 1f.dp,
        color = Color.Black,
        shape = RoundedCornerShape(size = 8.dp)
      )
  ) {
    val maxWidth = constraints.maxWidth.toFloat()
    val cellSize by remember(size) { mutableFloatStateOf(maxWidth / size.toFloat()) }
    val cellSizeDividerWidth by remember(size) { mutableFloatStateOf(cellSize / ceil(sqrt(size.toFloat()))) }
    val cellSizeDividerHeight by remember(size) { mutableFloatStateOf(cellSize / floor(sqrt(size.toFloat()))) }

    Column(modifier = Modifier.fillMaxWidth()) {
      for (i in board.indices) {
        Row(
          modifier = Modifier.height(IntrinsicSize.Min)
        ) {
          val cells = board[i]
          for (j in cells.indices) {
            Cell(
              boardCell = board[i][j],
              isSelected = false,
              isEditable = true,
              onClick = {
                onSelectCell(board[i][j])
              },
              modifier = Modifier
                .aspectRatio(1f)
                .weight(1f)
            )
            if (divider != 0 && ((j + 1) % divider == 0)) {
              VerticalDivider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier.fillMaxHeight()
              )
            }
          }
        }
        if (divider != 0 && ((i + 1) % divider == 0)) {
          HorizontalDivider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
              .height(1.dp)
              .fillMaxWidth(),
          )
        }
      }
    }
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

@Composable
internal fun InputPanel(
  modifier: Modifier = Modifier,
  onClick: (Int) -> Unit,
  size: Int,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    for (i in 1..size) {
      AutoSizeText(
        text = i.toString(),
        alignment = Alignment.Center,
        lineSpacingRatio = 1F,
        modifier = Modifier
          .weight(1f)
          .clickable { onClick(i) }
          .padding(4.dp),
      )
    }
  }
}

@Composable
internal fun Loading() {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Loading screen")
  Text(text = "Loading")
}

@Composable
internal fun Error(@StringRes message: Int) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Error screen")
  Text(text = stringResource(id = message))
}

@Composable
internal fun Empty() {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Empty screen")
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

@Preview(
  name = "Input Panel",
)
@Composable
internal fun InputPanelPreview() {
  InputPanel(size = 9, onClick = {})
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