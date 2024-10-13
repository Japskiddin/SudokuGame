package io.github.japskiddin.sudoku.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.Primary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.component.dialogBackground
import io.github.japskiddin.sudoku.feature.home.ui.components.ContinueDialog
import io.github.japskiddin.sudoku.feature.home.ui.components.DifficultyDialog
import io.github.japskiddin.sudoku.feature.home.ui.components.GameButton
import io.github.japskiddin.sudoku.feature.home.ui.components.OutlineText
import io.github.japskiddin.sudoku.feature.home.ui.logic.HomeViewModel
import io.github.japskiddin.sudoku.feature.home.ui.logic.UiState
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
public fun HomeScreen(modifier: Modifier = Modifier) {
    HomeScreen(
        modifier = modifier,
        viewModel = hiltViewModel()
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        modifier = modifier,
        state = state,
        currentYear = viewModel.currentYear,
        onStartButtonClick = { viewModel.onStartButtonClick() },
        onContinueButtonClick = { viewModel.onContinueButtonClick() },
        onSettingsButtonClick = { viewModel.onSettingsButtonClick() },
        onRecordsButtonClick = { viewModel.onRecordsButtonClick() },
        onContinueDialogButtonClick = { viewModel.onContinueDialogConfirm() },
        onStartGame = { viewModel.onDifficultyDialogConfirm() },
        onDismissContinueDialog = { viewModel.onDismissContinueDialog() },
        onDismissDifficultyDialog = { viewModel.onDismissDifficultyDialog() },
        onSwipeDifficultyLeft = { viewModel.onSelectPreviousGameDifficulty() },
        onSwipeDifficultyRight = { viewModel.onSelectNextGameDifficulty() },
        onSwipeTypeLeft = { viewModel.onSelectPreviousGameType() },
        onSwipeTypeRight = { viewModel.onSelectNextGameType() },
        onErrorClose = { viewModel.onCloseError() }
    )
}

@Suppress("LongParameterList")
@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: UiState,
    currentYear: String,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onRecordsButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onStartGame: () -> Unit,
    onContinueDialogButtonClick: () -> Unit,
    onDismissContinueDialog: () -> Unit,
    onDismissDifficultyDialog: () -> Unit,
    onSwipeDifficultyLeft: () -> Unit,
    onSwipeDifficultyRight: () -> Unit,
    onSwipeTypeLeft: () -> Unit,
    onSwipeTypeRight: () -> Unit,
    onErrorClose: () -> Unit
) {
    val screenModifier = Modifier
        .fillMaxSize()
        .then(modifier)
    when (state) {
        is UiState.Menu ->
            Menu(
                modifier = screenModifier,
                currentYear = currentYear,
                isShowContinueButton = state.isShowContinueButton,
                isShowContinueDialog = state.isShowContinueDialog,
                isShowDifficultyDialog = state.isShowDifficultyDialog,
                selectedDifficulty = state.selectedDifficulty,
                selectedType = state.selectedType,
                onStartButtonClick = onStartButtonClick,
                onContinueButtonClick = onContinueButtonClick,
                onRecordsButtonClick = onRecordsButtonClick,
                onSettingsButtonClick = onSettingsButtonClick,
                onStartGame = onStartGame,
                onContinueDialogButtonClick = onContinueDialogButtonClick,
                onDismissContinueDialog = onDismissContinueDialog,
                onDismissDifficultyDialog = onDismissDifficultyDialog,
                onSwipeDifficultyLeft = onSwipeDifficultyLeft,
                onSwipeDifficultyRight = onSwipeDifficultyRight,
                onSwipeTypeLeft = onSwipeTypeLeft,
                onSwipeTypeRight = onSwipeTypeRight
            )

        is UiState.Error ->
            Error(
                modifier = screenModifier,
                message = stringResource(
                    id = when (state.code) {
                        GameError.SUDOKU_NOT_GENERATED -> CoreUiR.string.err_generate_sudoku
                        else -> CoreUiR.string.err_unknown
                    }
                ),
                onClose = onErrorClose
            )

        is UiState.Loading ->
            Loading(
                modifier = screenModifier,
                resId = CoreUiR.string.preparing_game_please_wait
            )
    }
}

@Suppress("LongParameterList")
@Composable
private fun Menu(
    modifier: Modifier = Modifier,
    widthPercent: Float = .8f,
    isShowContinueButton: Boolean,
    isShowContinueDialog: Boolean,
    isShowDifficultyDialog: Boolean,
    selectedDifficulty: GameDifficulty,
    selectedType: GameType,
    currentYear: String,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onRecordsButtonClick: () -> Unit,
    onContinueDialogButtonClick: () -> Unit,
    onStartGame: () -> Unit,
    onDismissContinueDialog: () -> Unit,
    onDismissDifficultyDialog: () -> Unit,
    onSwipeDifficultyLeft: () -> Unit,
    onSwipeDifficultyRight: () -> Unit,
    onSwipeTypeLeft: () -> Unit,
    onSwipeTypeRight: () -> Unit
) {
    if (isShowContinueDialog) {
        ContinueDialog(
            onDismiss = onDismissContinueDialog,
            onContinueClick = onContinueDialogButtonClick
        )
    }

    if (isShowDifficultyDialog) {
        DifficultyDialog(
            selectedDifficulty = selectedDifficulty,
            selectedType = selectedType,
            onDismiss = onDismissDifficultyDialog,
            onStartClick = onStartGame,
            onSwipeDifficultyLeft = onSwipeDifficultyLeft,
            onSwipeDifficultyRight = onSwipeDifficultyRight,
            onSwipeTypeLeft = onSwipeTypeLeft,
            onSwipeTypeRight = onSwipeTypeRight
        )
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .paint(
                painter = painterResource(id = R.drawable.home_background),
                contentScale = ContentScale.Crop
            )
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(widthPercent)
                    .weight(1f)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                OutlineText(
                    text = stringResource(id = CoreUiR.string.app_name),
                    textStyle = MaterialTheme.typography.titleLarge,
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
                Spacer(modifier = Modifier.height(16.dp))
                GameButton(
                    icon = painterResource(id = R.drawable.ic_settings),
                    text = stringResource(id = CoreUiR.string.settings)
                ) { onSettingsButtonClick() }
                Spacer(modifier = Modifier.height(16.dp))
                GameButton(
                    icon = painterResource(id = R.drawable.ic_records),
                    text = stringResource(id = CoreUiR.string.records)
                ) { onRecordsButtonClick() }
            }
            OutlineText(
                text = currentYear,
                textStyle = MaterialTheme.typography.titleSmall,
                fillColor = Color.White,
                outlineColor = Color.Black
            )
        }
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    message: String,
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
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            GameButton(
                icon = painterResource(id = R.drawable.ic_close),
                text = stringResource(id = CoreUiR.string.close)
            ) { onClose() }
        }
    }
}

@Preview(
    name = "Home Content"
)
@Composable
private fun HomeContentPreview(
    @PreviewParameter(HomeStateProvider::class) state: UiState
) {
    SudokuTheme {
        HomeScreenContent(
            state = state,
            currentYear = "2024",
            onStartButtonClick = {},
            onContinueButtonClick = {},
            onRecordsButtonClick = {},
            onSettingsButtonClick = {},
            onStartGame = {},
            onContinueDialogButtonClick = {},
            onDismissContinueDialog = {},
            onDismissDifficultyDialog = {},
            onSwipeDifficultyLeft = {},
            onSwipeDifficultyRight = {},
            onSwipeTypeLeft = {},
            onSwipeTypeRight = {},
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
