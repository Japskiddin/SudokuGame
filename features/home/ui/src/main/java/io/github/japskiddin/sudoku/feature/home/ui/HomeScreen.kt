package io.github.japskiddin.sudoku.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.game.GameError
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.component.dialogBackground
import io.github.japskiddin.sudoku.core.ui.theme.Primary
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.home.domain.HomeViewModel
import io.github.japskiddin.sudoku.feature.home.domain.UiState
import io.github.japskiddin.sudoku.feature.home.ui.components.ContinueDialog
import io.github.japskiddin.sudoku.feature.home.ui.components.DifficultyDialog
import io.github.japskiddin.sudoku.feature.home.ui.components.Menu
import io.github.japskiddin.sudoku.feature.home.ui.components.OutlineText

@Composable
public fun HomeScreen(modifier: Modifier = Modifier) {
    HomeScreen(
        modifier = modifier,
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        modifier = modifier,
        state = state,
        currentYear = viewModel.currentYear,
        onStartGameClick = { viewModel.onStartClick() },
        onContinueGameClick = { viewModel.onContinueGameClick() },
        onSettingsClick = { viewModel.onSettingsClick() },
        onRecordsClick = { viewModel.onRecordsClick() },
        onContinueDialogButtonClick = {
            viewModel.onDismissContinueDialog()
            viewModel.onStartNewGame()
        },
        onDismissContinueDialog = { viewModel.onDismissContinueDialog() },
        onDismissDifficultyDialog = { viewModel.onDismissDifficultyDialog() }
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: UiState,
    currentYear: String,
    onStartGameClick: () -> Unit,
    onContinueGameClick: () -> Unit,
    onRecordsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onContinueDialogButtonClick: () -> Unit,
    onDismissContinueDialog: () -> Unit,
    onDismissDifficultyDialog: () -> Unit
) {
    val screenModifier = Modifier
        .fillMaxSize()
        .then(modifier)
    when (state) {
        is UiState.Menu ->
            HomeMenu(
                modifier = screenModifier,
                currentYear = currentYear,
                isShowContinueButton = state.isShowContinueButton,
                isShowContinueDialog = state.isShowContinueDialog,
                isShowDifficultyDialog = state.isShowDifficultyDialog,
                onStartGameClick = onStartGameClick,
                onContinueGameClick = onContinueGameClick,
                onRecordsClick = onRecordsClick,
                onSettingsClick = onSettingsClick,
                onContinueDialogButtonClick = onContinueDialogButtonClick,
                onDismissContinueDialog = onDismissContinueDialog,
                onDismissDifficultyDialog = onDismissDifficultyDialog
            )

        is UiState.Error ->
            HomeError(
                modifier = screenModifier,
                message = stringResource(
                    id = when (state.code) {
                        GameError.SUDOKU_NOT_GENERATED -> R.string.err_generate_sudoku
                        else -> io.github.japskiddin.sudoku.core.ui.R.string.err_unknown
                    }
                )
            )

        is UiState.Loading ->
            Loading(
                modifier = screenModifier,
                resId = R.string.preparing_game_please_wait
            )
    }
}

@Composable
private fun HomeMenu(
    modifier: Modifier = Modifier,
    widthPercent: Float = .8f,
    isShowContinueButton: Boolean,
    isShowContinueDialog: Boolean,
    isShowDifficultyDialog: Boolean,
    currentYear: String,
    onStartGameClick: () -> Unit,
    onContinueGameClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRecordsClick: () -> Unit,
    onContinueDialogButtonClick: () -> Unit,
    onDismissContinueDialog: () -> Unit,
    onDismissDifficultyDialog: () -> Unit
) {
    if (isShowContinueDialog) {
        ContinueDialog(
            onDismiss = onDismissContinueDialog,
            onContinueClick = onContinueDialogButtonClick
        )
    }

    if (isShowDifficultyDialog) {
        DifficultyDialog(
            onDismiss = onDismissDifficultyDialog,
            onStartClick = {},
            onSelectedDifficultyChanged = {},
            onSelectedTypeChanged = {}
        )
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .paint(
                painter = painterResource(id = R.drawable.home_background),
                contentScale = ContentScale.Crop
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Menu(
                modifier = Modifier
                    .fillMaxWidth(widthPercent)
                    .weight(1f),
                isShowContinueButton = isShowContinueButton,
                onStartGameClick = onStartGameClick,
                onContinueGameClick = onContinueGameClick,
                onSettingsClick = onSettingsClick,
                onRecordsClick = onRecordsClick
            )
            OutlineText(
                text = currentYear,
                fillColor = Color.White,
                outlineColor = Color.Black
            )
        }
    }
}

@Composable
private fun HomeError(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(Primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .dialogBackground()
        ) {
            Text(
                text = message,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                textAlign = TextAlign.Center
            )
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
            onStartGameClick = {},
            onContinueGameClick = {},
            onRecordsClick = {},
            onSettingsClick = {},
            onContinueDialogButtonClick = {},
            onDismissContinueDialog = {},
            onDismissDifficultyDialog = {}
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
