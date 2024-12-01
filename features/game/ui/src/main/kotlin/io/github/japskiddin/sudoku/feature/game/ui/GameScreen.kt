package io.github.japskiddin.sudoku.feature.game.ui

import android.app.Activity
import android.view.WindowManager
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import io.github.japskiddin.sudoku.feature.game.ui.component.ToolPanel
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameViewModel
import io.github.japskiddin.sudoku.feature.game.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.game.ui.logic.UiState
import io.github.japskiddin.sudoku.feature.game.ui.utils.ToolAction
import io.github.japskiddin.sudoku.feature.game.ui.utils.getSampleBoardForPreview
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
public fun GameScreen() {
    GameScreen(viewModel = hiltViewModel())
}

@Composable
private fun GameScreen(viewModel: GameViewModel) {
    BackHandler {
        viewModel.onAction(UiAction.Back)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    GameScreenContent(
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
    state: UiState,
    onSelectBoardCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
    onToolClick: (ToolAction) -> Unit,
    onCloseGame: () -> Unit,
) {
    val screenModifier = Modifier
        .fillMaxSize()
        .background(SudokuTheme.colors.primary)
    when (state) {
        is UiState.Game -> Game(
            state = state,
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
    state: UiState.Game,
    onSelectCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
    onToolClick: (ToolAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gameState = state.gameState
    val preferencesState = state.preferencesState
    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {
        if (preferencesState.isKeepScreenOn) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        onDispose {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .then(modifier)
            .safeDrawingPadding()
            .padding(12.dp)
    ) {
        InfoPanel(
            type = gameState.type,
            difficulty = gameState.difficulty,
            actions = gameState.actions,
            mistakes = gameState.mistakes,
            time = gameState.time,
            isShowTimer = preferencesState.isShowTimer,
            isMistakesLimit = preferencesState.isMistakesLimit
        )
        Spacer(modifier = Modifier.height(12.dp))
        GameBoard(
            board = gameState.board,
            selectedCell = gameState.selectedCell,
            onSelectCell = { boardCell -> onSelectCell(boardCell) },
        )
        Spacer(modifier = Modifier.height(6.dp))
        ToolPanel(onToolClick = onToolClick)
        Spacer(modifier = Modifier.height(6.dp))
        InputPanel(
            board = gameState.board,
            showRemainingNumbers = preferencesState.isShowRemainingNumbers,
            onClick = onInputCell
        )
    }
}

@Composable
private fun Error(
    errorCode: GameError,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val message = stringResource(id = errorCode.toStringRes())

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(SudokuTheme.colors.primary)
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            BasicText(
                text = message,
                style = SudokuTheme.typography.bodyLarge.copy(
                    color = SudokuTheme.colors.primary,
                    textAlign = TextAlign.Center
                ),
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
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(SudokuTheme.colors.primary)
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            BasicText(
                text = "",
                style = SudokuTheme.typography.bodyLarge.copy(
                    color = SudokuTheme.colors.primary,
                    textAlign = TextAlign.Center
                ),
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
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(SudokuTheme.colors.primary)
            .safeDrawingPadding()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            BasicText(
                text = stringResource(id = CoreUiR.string.game_completed),
                style = SudokuTheme.typography.bodyLarge.copy(
                    color = SudokuTheme.colors.primary,
                    textAlign = TextAlign.Center
                ),
            )
        }
    }
}

@Preview(
    name = "Game Content",
    device = Devices.PIXEL_2,
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
