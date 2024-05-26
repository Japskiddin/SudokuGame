package io.github.japskiddin.sudoku.feature.game

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.feature.component.GameBoard
import io.github.japskiddin.sudoku.feature.component.autosizetext.AutoSizeText

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
  name = "Input Panel",
)
@Composable
internal fun InputPanelPreview() {
  InputPanel(size = 9, onClick = {})
}