package io.github.japskiddin.sudoku.feature.game.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.game.GameDifficulty
import io.github.japskiddin.sudoku.core.game.GameError
import io.github.japskiddin.sudoku.core.game.GameType
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.theme.Primary
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.feature.game.domain.GameState
import io.github.japskiddin.sudoku.feature.game.domain.GameViewModel
import io.github.japskiddin.sudoku.feature.game.domain.UiState
import io.github.japskiddin.sudoku.feature.game.ui.component.GameBoard
import io.github.japskiddin.sudoku.feature.game.ui.component.autosizetext.AutoSizeText
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
public fun GameScreen(modifier: Modifier = Modifier) {
    GameScreen(modifier = modifier, viewModel = hiltViewModel())
}

@Composable
internal fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GameScreenContent(
        modifier = modifier,
        state = state,
        onSelectBoardCell = { boardCell -> viewModel.onUpdateSelectedBoardCell(boardCell) },
        onInputCell = { num -> viewModel.onInputCell(num) }
    )
}

@Composable
private fun GameScreenContent(
    modifier: Modifier = Modifier,
    state: UiState,
    onSelectBoardCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
) {
    val screenModifier = Modifier
        .fillMaxSize()
        .then(modifier)
        .background(Primary)
    when (state) {
        is UiState.Game -> Game(
            state = state.gameState,
            onSelectCell = onSelectBoardCell,
            onInputCell = onInputCell,
            modifier = screenModifier
        )

        is UiState.Loading -> Loading(
            modifier = screenModifier,
            resId = R.string.level_creation
        )

        is UiState.Error -> Error(
            message = stringResource(
                id = when (state.code) {
                    GameError.BOARD_NOT_FOUND -> R.string.err_generate_level
                    else -> io.github.japskiddin.sudoku.core.ui.R.string.err_unknown
                }
            ),
            modifier = screenModifier
        )
    }
}

@Composable
private fun Game(
    modifier: Modifier = Modifier,
    state: GameState,
    onSelectCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.then(modifier)
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
            onClick = onInputCell
        )
    }
}

@Composable
private fun InputPanel(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    size: Int
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
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.then(modifier)
    ) {
        Text(text = message)
    }
}

@Preview(
    name = "Input Panel"
)
@Composable
private fun InputPanelPreview() {
    InputPanel(size = 9, onClick = {})
}

@Preview(
    name = "Game Content"
)
@Composable
private fun GameContentPreview(
    @PreviewParameter(GameStateProvider::class) state: UiState,
) {
    SudokuTheme {
        GameScreenContent(
            state = state,
            onSelectBoardCell = {},
            onInputCell = { _ -> }
        )
    }
}

private class GameStateProvider : PreviewParameterProvider<UiState> {
    private val parser = SudokuParser()
    private val board = Board(
        initialBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        solvedBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        difficulty = GameDifficulty.INTERMEDIATE,
        type = GameType.DEFAULT9X9
    )

    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Game(
                gameState = GameState(
                    board = parser.parseBoard(
                        board = board.initialBoard,
                        gameType = board.type
                    ).map { item -> item.toImmutableList() }
                        .toImmutableList(),
                    notes = persistentListOf(),
                    selectedCell = BoardCell(
                        row = 3,
                        col = 2,
                        value = 3
                    )
                )
            ),
            UiState.Loading,
            UiState.Error(code = GameError.BOARD_NOT_FOUND)
        )
}
