package io.github.japskiddin.sudoku.feature.game.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.Primary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.toStringRes
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.component.LifecycleEventListener
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.utils.dialogBackground
import io.github.japskiddin.sudoku.feature.game.ui.component.GameBoard
import io.github.japskiddin.sudoku.feature.game.ui.component.InfoPanel
import io.github.japskiddin.sudoku.feature.game.ui.component.InputPanel
import io.github.japskiddin.sudoku.feature.game.ui.component.ResetDialog
import io.github.japskiddin.sudoku.feature.game.ui.component.ToolPanel
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameViewModel
import io.github.japskiddin.sudoku.feature.game.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.game.ui.logic.UiState
import io.github.japskiddin.sudoku.feature.game.ui.utils.ToolAction
import io.github.japskiddin.sudoku.feature.game.ui.utils.getSampleBoardForPreview
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

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
    BackHandler {
        viewModel.onAction(UiAction.Back)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GameScreenContent(
        modifier = modifier,
        state = state,
        onSelectBoardCell = { cell -> viewModel.onAction(UiAction.SelectBoardCell(cell)) },
        onInputCell = { value -> viewModel.onAction(UiAction.InputCell(value)) },
        onToolClick = { action ->
            val uiAction = when (action) {
                ToolAction.ERASER -> UiAction.EraseBoardCell
                ToolAction.NOTE -> UiAction.Note
                ToolAction.RESET -> UiAction.ResetBoard
                ToolAction.UNDO -> UiAction.Undo
                ToolAction.REDO -> UiAction.Redo
            }
            viewModel.onAction(uiAction)
        },
        onCloseGame = { viewModel.onAction(UiAction.Exit) }
    )

    LifecycleEventListener {
        when (it) {
            Lifecycle.Event.ON_RESUME -> {
                if (state is UiState.Game) {
                    viewModel.onAction(UiAction.ResumeGame)
                }
            }

            Lifecycle.Event.ON_PAUSE -> {
                if (state is UiState.Game) {
                    viewModel.onAction(UiAction.PauseGame)
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun GameScreenContent(
    modifier: Modifier = Modifier,
    state: UiState,
    onSelectBoardCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
    onToolClick: (ToolAction) -> Unit,
    onCloseGame: () -> Unit
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
            onToolClick = onToolClick,
            modifier = screenModifier
        )

        is UiState.Complete -> Complete(
            modifier = screenModifier
        )

        is UiState.Fail -> Fail(
            modifier = screenModifier,
            onClose = onCloseGame
        )

        is UiState.Loading -> Loading(
            modifier = screenModifier,
            resId = CoreUiR.string.level_creation
        )

        is UiState.Error -> Error(
            errorCode = state.code,
            modifier = screenModifier,
            onClose = onCloseGame
        )
    }
}

@Composable
private fun Game(
    modifier: Modifier = Modifier,
    state: GameUiState,
    onSelectCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
    onToolClick: (ToolAction) -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        ResetDialog(
            onDismiss = { showResetDialog = false },
            onConfirm = {
                showResetDialog = false
                onToolClick(ToolAction.RESET)
            }
        )
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .then(modifier)
            .safeDrawingPadding()
            .padding(12.dp)
    ) {
        InfoPanel(
            type = state.type,
            difficulty = state.difficulty,
            actions = state.actions,
            mistakes = state.mistakes,
            time = state.time
        )
        Spacer(modifier = Modifier.height(12.dp))
        GameBoard(
            board = state.board,
            selectedCell = state.selectedCell,
            onSelectCell = { boardCell -> onSelectCell(boardCell) },
        )
        Spacer(modifier = Modifier.height(6.dp))
        ToolPanel(
            onToolClick = { action ->
                if (action == ToolAction.RESET) {
                    showResetDialog = true
                } else {
                    onToolClick(action)
                }
            }
        )
        Spacer(modifier = Modifier.height(6.dp))
        InputPanel(
            board = state.board,
            onClick = onInputCell
        )
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    errorCode: GameError,
    onClose: () -> Unit
) {
    val message = stringResource(id = errorCode.toStringRes())

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(Primary)
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            GameButton(
                icon = painterResource(id = CoreUiR.drawable.ic_close),
                text = stringResource(id = CoreUiR.string.close)
            ) { onClose() }
        }
    }
}

@Composable
private fun Fail(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(Primary)
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.bodyLarge,
                color = Primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            GameButton(
                icon = painterResource(id = CoreUiR.drawable.ic_close),
                text = stringResource(id = CoreUiR.string.close)
            ) { onClose() }
        }
    }
}

@Composable
private fun Complete(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(Primary)
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            Text(
                text = stringResource(id = CoreUiR.string.game_completed),
                style = MaterialTheme.typography.bodyLarge,
                color = Primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    name = "Game Content",
    device = Devices.PIXEL_2
)
@Composable
private fun GameContentPreview(
    @PreviewParameter(UiStateProvider::class) state: UiState,
) {
    SudokuTheme {
        GameScreenContent(
            state = state,
            onSelectBoardCell = {},
            onInputCell = { _ -> },
            onToolClick = {},
            onCloseGame = {}
        )
    }
}

private class UiStateProvider : PreviewParameterProvider<UiState> {
    private val gameState: GameUiState = GameUiState(
        board = getSampleBoardForPreview(),
        selectedCell = BoardCell(
            row = 3,
            col = 2,
            value = 3
        ),
        type = GameType.DEFAULT9X9,
        difficulty = GameDifficulty.INTERMEDIATE,
        actions = 0,
        mistakes = 0,
        time = 0L
    )

    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Game(gameState = gameState),
            UiState.Loading,
            UiState.Error(code = GameError.BOARD_NOT_FOUND),
            UiState.Complete,
            UiState.Fail
        )
}
