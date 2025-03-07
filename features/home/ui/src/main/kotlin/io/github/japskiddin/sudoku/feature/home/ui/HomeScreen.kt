package io.github.japskiddin.sudoku.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.toStringRes
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameMode
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.component.GameDialog
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.component.OutlineText
import io.github.japskiddin.sudoku.core.ui.utils.SudokuPreview
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape
import io.github.japskiddin.sudoku.core.ui.utils.panelBackground
import io.github.japskiddin.sudoku.feature.home.ui.components.ContinueDialogContent
import io.github.japskiddin.sudoku.feature.home.ui.components.DifficultyDialogContent
import io.github.japskiddin.sudoku.feature.home.ui.logic.HomeViewModel
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiState
import io.github.japskiddin.sudoku.feature.home.ui.utils.getPackageInfo
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
public fun HomeScreen() {
    HomeScreen(viewModel = hiltViewModel())
}

@Composable
private fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        state = state,
        currentYear = viewModel.currentYear,
        onContinueButtonClick = { viewModel.onAction(UiAction.ContinueGame) },
        onSettingsButtonClick = { viewModel.onAction(UiAction.ShowSettings) },
        onHistoryButtonClick = { viewModel.onAction(UiAction.ShowHistory) },
        onPrepareGame = { mode ->
            viewModel.onAction(
                UiAction.PrepareNewGame(mode)
            )
        },
        onErrorClose = { viewModel.onAction(UiAction.CloseError) }
    )
}

@Suppress("LongParameterList")
@Composable
private fun HomeScreenContent(
    state: UiState,
    currentYear: String,
    onContinueButtonClick: () -> Unit,
    onHistoryButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onPrepareGame: (GameMode) -> Unit,
    onErrorClose: () -> Unit,
) {
    val screenModifier = Modifier.fillMaxSize()

    when (state) {
        is UiState.Menu -> Menu(
            modifier = screenModifier,
            currentYear = currentYear,
            isShowContinueButton = state.isShowContinueButton,
            gameMode = state.gameMode,
            onPrepareGame = onPrepareGame,
            onContinueButtonClick = onContinueButtonClick,
            onHistoryButtonClick = onHistoryButtonClick,
            onSettingsButtonClick = onSettingsButtonClick
        )

        is UiState.Error -> Error(
            modifier = screenModifier,
            errorCode = state.code,
            onClose = onErrorClose
        )

        is UiState.Loading -> Loading(
            modifier = screenModifier,
            resId = CoreUiR.string.preparing_game_please_wait
        )
    }
}

@Suppress("LongParameterList")
@Composable
private fun Menu(
    gameMode: GameMode,
    currentYear: String,
    isShowContinueButton: Boolean,
    onContinueButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onHistoryButtonClick: () -> Unit,
    onPrepareGame: (GameMode) -> Unit,
    modifier: Modifier = Modifier,
    screenWidthPercent: Float = .8f,
) {
    var showContinueDialog by rememberSaveable { mutableStateOf(false) }
    var showDifficultyDialog by rememberSaveable { mutableStateOf(false) }

    if (showContinueDialog) {
        GameDialog(
            showDialog = showContinueDialog,
            onDismiss = { showContinueDialog = false }
        ) {
            ContinueDialogContent(
                onConfirm = {
                    showContinueDialog = false
                    showDifficultyDialog = true
                }
            )
        }
    }

    if (showDifficultyDialog) {
        GameDialog(
            showDialog = showDifficultyDialog,
            onDismiss = { showDifficultyDialog = false }
        ) {
            DifficultyDialogContent(
                gameMode = gameMode,
                onConfirm = { mode ->
                    showDifficultyDialog = false
                    onPrepareGame(mode)
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .paint(
                painter = painterResource(id = R.drawable.home_background),
                contentScale = ContentScale.Crop
            )
            .safeDrawingPadding()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(screenWidthPercent)
        ) {
            val menuModifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight()
            val onStartButtonClick: () -> Unit = {
                if (isShowContinueButton) {
                    showContinueDialog = true
                } else {
                    showDifficultyDialog = true
                }
            }

            if (isLandscape()) {
                MenuLandscape(
                    modifier = menuModifier,
                    isShowContinueButton = isShowContinueButton,
                    onStartButtonClick = onStartButtonClick,
                    onContinueButtonClick = onContinueButtonClick,
                    onSettingsButtonClick = onSettingsButtonClick,
                    onHistoryButtonClick = onHistoryButtonClick
                )
            } else {
                MenuPortrait(
                    modifier = menuModifier,
                    isShowContinueButton = isShowContinueButton,
                    onStartButtonClick = onStartButtonClick,
                    onContinueButtonClick = onContinueButtonClick,
                    onSettingsButtonClick = onSettingsButtonClick,
                    onHistoryButtonClick = onHistoryButtonClick
                )
            }

            val context = LocalContext.current
            val versionName: String = context.getPackageInfo().versionName.toString()
            OutlineText(
                text = versionName,
                textStyle = SudokuTheme.typography.info,
                fillColor = Color.White,
                outlineColor = Color.Black
            )
            OutlineText(
                text = currentYear,
                textStyle = SudokuTheme.typography.info,
                fillColor = Color.White,
                outlineColor = Color.Black
            )
        }
    }
}

@Composable
private fun MenuLandscape(
    isShowContinueButton: Boolean,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onHistoryButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlineText(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.app_name),
            textStyle = SudokuTheme.typography.appTitle,
            fillColor = Color.White,
            outlineColor = Color.Black,
            outlineWidth = 6.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (isShowContinueButton) {
                GameButton(
                    icon = painterResource(id = R.drawable.ic_start),
                    text = stringResource(id = CoreUiR.string.continue_game)
                ) { onContinueButtonClick() }
                Spacer(modifier = Modifier.height(12.dp))
            }
            GameButton(
                icon = painterResource(id = R.drawable.ic_start),
                text = stringResource(id = CoreUiR.string.start_game)
            ) { onStartButtonClick() }
            Spacer(modifier = Modifier.height(12.dp))
            GameButton(
                icon = painterResource(id = R.drawable.ic_settings),
                text = stringResource(id = CoreUiR.string.settings)
            ) { onSettingsButtonClick() }
            Spacer(modifier = Modifier.height(12.dp))
            GameButton(
                icon = painterResource(id = R.drawable.ic_history),
                text = stringResource(id = CoreUiR.string.history)
            ) { onHistoryButtonClick() }
        }
    }
}

@Composable
private fun MenuPortrait(
    isShowContinueButton: Boolean,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onHistoryButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        OutlineText(
            text = stringResource(id = CoreUiR.string.app_name),
            textStyle = SudokuTheme.typography.appTitle,
            fillColor = Color.White,
            outlineColor = Color.Black,
            outlineWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(36.dp))
        if (isShowContinueButton) {
            GameButton(
                icon = painterResource(id = R.drawable.ic_start),
                text = stringResource(id = CoreUiR.string.continue_game)
            ) { onContinueButtonClick() }
            Spacer(modifier = Modifier.height(36.dp))
        }
        GameButton(
            icon = painterResource(id = R.drawable.ic_start),
            text = stringResource(id = CoreUiR.string.start_game)
        ) { onStartButtonClick() }
        Spacer(modifier = Modifier.height(12.dp))
        GameButton(
            icon = painterResource(id = R.drawable.ic_settings),
            text = stringResource(id = CoreUiR.string.settings)
        ) { onSettingsButtonClick() }
        Spacer(modifier = Modifier.height(12.dp))
        GameButton(
            icon = painterResource(id = R.drawable.ic_history),
            text = stringResource(id = CoreUiR.string.history)
        ) { onHistoryButtonClick() }
    }
}

@Composable
private fun Error(
    errorCode: GameError,
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
            modifier = Modifier.panelBackground()
        ) {
            BasicText(
                text = stringResource(id = errorCode.toStringRes()),
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

@Suppress("UnusedPrivateMember")
@SudokuPreview
@Composable
private fun HomeContentPreview(
    @PreviewParameter(HomeStateProvider::class) state: UiState
) {
    SudokuTheme {
        HomeScreenContent(
            state = state,
            currentYear = "2024",
            onContinueButtonClick = {},
            onHistoryButtonClick = {},
            onSettingsButtonClick = {},
            onPrepareGame = { _ -> },
            onErrorClose = {}
        )
    }
}

private class HomeStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Menu(),
            UiState.Loading,
            UiState.Error(code = GameError.SUDOKU_NOT_GENERATED)
        )
}
