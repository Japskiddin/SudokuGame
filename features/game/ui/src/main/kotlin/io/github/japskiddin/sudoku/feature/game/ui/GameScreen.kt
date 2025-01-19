package io.github.japskiddin.sudoku.feature.game.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape
import io.github.japskiddin.sudoku.feature.game.ui.component.GameBoard
import io.github.japskiddin.sudoku.feature.game.ui.component.InfoPanel
import io.github.japskiddin.sudoku.feature.game.ui.component.InputPanel
import io.github.japskiddin.sudoku.feature.game.ui.component.KeepScreenOn
import io.github.japskiddin.sudoku.feature.game.ui.component.ToolPanel
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameUiState
import io.github.japskiddin.sudoku.feature.game.ui.logic.GameViewModel
import io.github.japskiddin.sudoku.feature.game.ui.logic.PreferencesUiState
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
        onCloseGame = { viewModel.onAction(UiAction.Exit) },
        onSettingsClick = { viewModel.onAction(UiAction.ShowSettings) },
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
    onSettingsClick: () -> Unit,
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
            onSettingsClick = onSettingsClick,
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
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gameState = state.gameState
    val preferencesState = state.preferencesState
    KeepScreenOn(isEnabled = preferencesState.isKeepScreenOn)

    Column(
        modifier = Modifier
            .then(modifier)
            .safeDrawingPadding()
            .padding(12.dp)
    ) {
        val gameModifier = Modifier.fillMaxSize()
        if (isLandscape()) {
            LandscapeGameContent(
                gameState = gameState,
                preferencesState = preferencesState,
                onSelectCell = onSelectCell,
                onInputCell = onInputCell,
                onToolClick = onToolClick,
                onSettingsClick = onSettingsClick,
                modifier = gameModifier,
            )
        } else {
            PortraitGameContent(
                gameState = gameState,
                preferencesState = preferencesState,
                onSelectCell = onSelectCell,
                onInputCell = onInputCell,
                onToolClick = onToolClick,
                onSettingsClick = onSettingsClick,
                modifier = gameModifier,
            )
        }
    }
}

// @Composable
// private fun LandscapeGameContent(
//    gameState: GameUiState,
//    preferencesState: PreferencesUiState,
//    onSelectCell: (BoardCell) -> Unit,
//    onInputCell: (Int) -> Unit,
//    onToolClick: (ToolAction) -> Unit,
//    onSettingsClick: () -> Unit,
//    modifier: Modifier = Modifier,
// ) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//    ) {
//        Column {
//            Menu(
//                onSettingsClick = onSettingsClick,
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            InfoPanel(
//                type = gameState.type,
//                difficulty = gameState.difficulty,
//                actions = gameState.actions,
//                mistakes = gameState.mistakes,
//                time = gameState.time,
//                isShowTimer = preferencesState.isShowTimer,
//                isMistakesLimit = preferencesState.isMistakesLimit
//            )
//        }
//        Spacer(modifier = Modifier.weight(1f))
//        GameBoard(
//            board = gameState.board,
//            selectedCell = gameState.selectedCell,
//            isErrorsHighlight = preferencesState.isHighlightErrorCells,
//            isIdenticalNumbersHighlight = preferencesState.isHighlightSimilarCells,
//            isPositionCells = preferencesState.isHighlightSelectedCell,
//            modifier = Modifier
//                .fillMaxHeight()
//                .wrapContentWidth(),
//        ) { boardCell ->
//            onSelectCell(boardCell)
//        }
//        Spacer(modifier = Modifier.weight(1f))
//        ToolPanel(onToolClick = onToolClick)
//        Spacer(modifier = Modifier.width(12.dp))
//        InputPanel(
//            board = gameState.board,
//            showRemainingNumbers = preferencesState.isShowRemainingNumbers,
//            onClick = onInputCell
//        )
//    }
// }

@Composable
private fun LandscapeGameContent(
    gameState: GameUiState,
    preferencesState: PreferencesUiState,
    onSelectCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
    onToolClick: (ToolAction) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        GameBoard(
            board = gameState.board,
            selectedCell = gameState.selectedCell,
            isErrorsHighlight = preferencesState.isHighlightErrorCells,
            isIdenticalNumbersHighlight = preferencesState.isHighlightSimilarCells,
            isPositionCells = preferencesState.isHighlightSelectedCell,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
        ) { boardCell ->
            onSelectCell(boardCell)
        }
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Menu(
                modifier = Modifier.align(Alignment.End),
                onSettingsClick = onSettingsClick,
            )
            Spacer(modifier = Modifier.height(6.dp))
            InfoPanel(
                type = gameState.type,
                difficulty = gameState.difficulty,
                actions = gameState.actions,
                mistakes = gameState.mistakes,
                time = gameState.time,
                isShowTimer = preferencesState.isShowTimer,
                isMistakesLimit = preferencesState.isMistakesLimit
            )
            Spacer(modifier = Modifier.weight(1f))
            ToolPanel(onToolClick = onToolClick)
            Spacer(modifier = Modifier.height(6.dp))
            InputPanel(
                board = gameState.board,
                showRemainingNumbers = preferencesState.isShowRemainingNumbers,
                onClick = onInputCell
            )
        }
    }
}

@Composable
private fun PortraitGameContent(
    gameState: GameUiState,
    preferencesState: PreferencesUiState,
    onSelectCell: (BoardCell) -> Unit,
    onInputCell: (Int) -> Unit,
    onToolClick: (ToolAction) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Menu(
            modifier = Modifier.align(Alignment.End),
            onSettingsClick = onSettingsClick,
        )
        Spacer(modifier = Modifier.height(6.dp))
        InfoPanel(
            type = gameState.type,
            difficulty = gameState.difficulty,
            actions = gameState.actions,
            mistakes = gameState.mistakes,
            time = gameState.time,
            isShowTimer = preferencesState.isShowTimer,
            isMistakesLimit = preferencesState.isMistakesLimit
        )
        Spacer(modifier = Modifier.weight(1f))
        GameBoard(
            board = gameState.board,
            selectedCell = gameState.selectedCell,
            isErrorsHighlight = preferencesState.isHighlightErrorCells,
            isIdenticalNumbersHighlight = preferencesState.isHighlightSimilarCells,
            isPositionCells = preferencesState.isHighlightSelectedCell,
            modifier = Modifier.fillMaxWidth(),
        ) { boardCell ->
            onSelectCell(boardCell)
        }
        Spacer(modifier = Modifier.weight(1f))
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
private fun Menu(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .clickable { onSettingsClick() },
            painter = painterResource(id = R.drawable.ic_menu_settings),
            contentDescription = stringResource(id = CoreUiR.string.settings)
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
                style = SudokuTheme.typography.panel.copy(
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
                style = SudokuTheme.typography.panel.copy(
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
                style = SudokuTheme.typography.panel.copy(
                    color = SudokuTheme.colors.primary,
                    textAlign = TextAlign.Center
                ),
            )
        }
    }
}

@Preview(
    name = "Game Content - Portrait mode",
    device = Devices.PIXEL_2,
)
@Preview(
    name = "Game Content - Landscape mode",
    widthDp = 732,
    heightDp = 412,
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
            onCloseGame = {},
            onSettingsClick = {},
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
