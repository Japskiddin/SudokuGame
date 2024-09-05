package io.github.japskiddin.sudoku.feature.game.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import io.github.japskiddin.sudoku.core.designsystem.theme.Primary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.feature.game.ui.component.GameBoard
import io.github.japskiddin.sudoku.feature.game.ui.component.InputPanel
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameViewModel
import io.github.japskiddin.sudoku.feature.game.ui.logic.UiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
public fun GameScreen(modifier: Modifier = Modifier) {
    GameScreen(
        modifier = modifier,
        viewModel = hiltViewModel()
    )
}

@Composable
private fun GameScreen(
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
        modifier = Modifier
            .then(modifier)
            .safeDrawingPadding()
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
            board = state.board,
            onClick = onInputCell
        )
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .safeContentPadding()
    ) {
        Text(text = message)
    }
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
    private val parsedBoard = parser.parseBoard(
        board = board.initialBoard,
        gameType = board.type
    ).map { item -> item.toImmutableList() }
        .toImmutableList()

    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Game(
                gameState = GameState(
                    board = parsedBoard,
                    initialBoard = parsedBoard,
                    solvedBoard = parsedBoard,
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
