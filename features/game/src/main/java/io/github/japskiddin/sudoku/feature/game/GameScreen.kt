package io.github.japskiddin.sudoku.feature.game

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.theme.Primary
import io.github.japskiddin.sudoku.feature.component.GameBoard
import io.github.japskiddin.sudoku.feature.component.GameBoardUiPreviewProvider
import io.github.japskiddin.sudoku.feature.component.autosizetext.AutoSizeText

private const val TAG = "Game UI"

@Composable
fun GameScreen(modifier: Modifier = Modifier) {
  GameScreen(modifier = modifier, viewModel = hiltViewModel())
}

@Composable
internal fun GameScreen(
  modifier: Modifier = Modifier,
  viewModel: GameViewModel
) {
  val state by viewModel.uiState.collectAsState()
  GameScreenContent(
    modifier = modifier,
    state = state,
    onSelectBoardCell = { boardCell -> viewModel.onUpdateSelectedBoardCell(boardCell) },
    onInputCell = { cell, item -> viewModel.onInputCell(cell, item) },
  )
}

@Composable
private fun GameScreenContent(
  modifier: Modifier = Modifier,
  state: UiState,
  onSelectBoardCell: (BoardCell) -> Unit,
  onInputCell: (Pair<Int, Int>, Int) -> Unit,
) {
  val screenModifier = Modifier
    .fillMaxSize()
    .then(modifier)
    .background(Primary)
  when (state) {
    is UiState.Success -> Game(
      state = state.gameState,
      onSelectCell = onSelectBoardCell,
      onInputCell = onInputCell,
      modifier = screenModifier,
    )

    is UiState.Loading -> Loading(
      modifier = screenModifier,
      resId = state.message,
    )

    is UiState.Error -> Error(
      message = state.message,
      modifier = screenModifier,
    )
  }
}

@Composable
private fun Game(
  modifier: Modifier = Modifier,
  state: GameState,
  onSelectCell: (BoardCell) -> Unit,
  onInputCell: (Pair<Int, Int>, Int) -> Unit,
) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Game screen")

  Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.then(modifier),
  ) {
    GameBoard(
      board = state.board,
      selectedCell = state.selectedCell,
      onSelectCell = { boardCell -> onSelectCell(boardCell) },
      modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth()
    )
    InputPanel(
      size = state.board.size,
      onClick = { item -> }
    )
  }
}

@Composable
private fun InputPanel(
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
private fun Error(
  modifier: Modifier = Modifier,
  @StringRes message: Int,
) {
  if (BuildConfig.DEBUG) Log.d(TAG, "Composing Error screen")
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.then(modifier),
  ) {
    Text(text = stringResource(id = message))
  }
}

@Preview(
  name = "Game Screen"
)
@Composable
private fun GameScreenPreview(
  @PreviewParameter(GameBoardUiPreviewProvider::class) state: GameState
) {
  GameScreenContent(
    state = UiState.Success(gameState = state),
    onSelectBoardCell = {},
    onInputCell = { _cell, _value -> })
}

@Preview(
  name = "Input Panel",
)
@Composable
private fun InputPanelPreview() {
  InputPanel(size = 9, onClick = {})
}