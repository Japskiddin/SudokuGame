package io.github.japskiddin.sudoku.feature.home.ui

import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.Primary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.toStringRes
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameError
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.GameButton
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.component.OutlineText
import io.github.japskiddin.sudoku.core.ui.utils.dialogBackground
import io.github.japskiddin.sudoku.feature.home.ui.components.ContinueDialog
import io.github.japskiddin.sudoku.feature.home.ui.components.DifficultyDialog
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
        onStartButtonClick = { viewModel.onAction(UiAction.PrepareNewGame) },
        onContinueButtonClick = { viewModel.onAction(UiAction.ContinueGame) },
        onSettingsButtonClick = { viewModel.onAction(UiAction.ShowSettings) },
        onRecordsButtonClick = { viewModel.onAction(UiAction.ShowRecords) },
        onContinueDialogConfirm = { viewModel.onAction(UiAction.ContinueDialogConfirm) },
        onDifficultyDialogConfirm = { difficulty, type ->
            viewModel.onAction(
                UiAction.DifficultyDialogConfirm(
                    difficulty,
                    type
                )
            )
        },
        onDismissContinueDialog = { viewModel.onAction(UiAction.ContinueDialogDismiss) },
        onDismissDifficultyDialog = { viewModel.onAction(UiAction.DifficultyDialogDismiss) },
        onErrorClose = { viewModel.onAction(UiAction.CloseError) }
    )
}

@Suppress("LongParameterList")
@Composable
private fun HomeScreenContent(
    state: UiState,
    currentYear: String,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onRecordsButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onDifficultyDialogConfirm: (GameDifficulty, GameType) -> Unit,
    onContinueDialogConfirm: () -> Unit,
    onDismissContinueDialog: () -> Unit,
    onDismissDifficultyDialog: () -> Unit,
    onErrorClose: () -> Unit
) {
    val screenModifier = Modifier.fillMaxSize()

    when (state) {
        is UiState.Menu -> Menu(
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
            onDifficultyDialogConfirm = onDifficultyDialogConfirm,
            onContinueDialogConfirm = onContinueDialogConfirm,
            onDismissContinueDialog = onDismissContinueDialog,
            onDismissDifficultyDialog = onDismissDifficultyDialog
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
    modifier: Modifier = Modifier,
    screenWidthPercent: Float = .8f,
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
    onContinueDialogConfirm: () -> Unit,
    onDifficultyDialogConfirm: (GameDifficulty, GameType) -> Unit,
    onDismissContinueDialog: () -> Unit,
    onDismissDifficultyDialog: () -> Unit
) {
    if (isShowContinueDialog) {
        ContinueDialog(
            onDismiss = onDismissContinueDialog,
            onConfirm = onContinueDialogConfirm
        )
    }

    if (isShowDifficultyDialog) {
        DifficultyDialog(
            selectedDifficulty = selectedDifficulty,
            selectedType = selectedType,
            onDismiss = onDismissDifficultyDialog,
            onConfirm = onDifficultyDialogConfirm
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
            val configuration = LocalConfiguration.current

            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                MenuLandscape(
                    modifier = menuModifier,
                    isShowContinueButton = isShowContinueButton,
                    onStartButtonClick = onStartButtonClick,
                    onContinueButtonClick = onContinueButtonClick,
                    onSettingsButtonClick = onSettingsButtonClick,
                    onRecordsButtonClick = onRecordsButtonClick
                )
            } else {
                MenuPortrait(
                    modifier = menuModifier,
                    isShowContinueButton = isShowContinueButton,
                    onStartButtonClick = onStartButtonClick,
                    onContinueButtonClick = onContinueButtonClick,
                    onSettingsButtonClick = onSettingsButtonClick,
                    onRecordsButtonClick = onRecordsButtonClick
                )
            }

            val versionName: String = LocalContext.current.getPackageInfo().versionName.toString()
            OutlineText(
                text = versionName,
                textStyle = MaterialTheme.typography.titleSmall,
                fillColor = Color.White,
                outlineColor = Color.Black
            )
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
private fun MenuLandscape(
    modifier: Modifier = Modifier,
    isShowContinueButton: Boolean,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onRecordsButtonClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlineText(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.app_name),
            textStyle = MaterialTheme.typography.titleLarge,
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
                icon = painterResource(id = R.drawable.ic_records),
                text = stringResource(id = CoreUiR.string.records)
            ) { onRecordsButtonClick() }
        }
    }
}

@Composable
private fun MenuPortrait(
    modifier: Modifier = Modifier,
    isShowContinueButton: Boolean,
    onStartButtonClick: () -> Unit,
    onContinueButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    onRecordsButtonClick: () -> Unit
) {
    Column(
        modifier = modifier,
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
        Spacer(modifier = Modifier.height(12.dp))
        GameButton(
            icon = painterResource(id = R.drawable.ic_settings),
            text = stringResource(id = CoreUiR.string.settings)
        ) { onSettingsButtonClick() }
        Spacer(modifier = Modifier.height(12.dp))
        GameButton(
            icon = painterResource(id = R.drawable.ic_records),
            text = stringResource(id = CoreUiR.string.records)
        ) { onRecordsButtonClick() }
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    errorCode: GameError,
    onClose: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .background(Primary)
            .safeDrawingPadding()
    ) {
        val configuration = LocalConfiguration.current
        val widthPercent = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            .6f
        } else {
            1f
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(widthPercent)
                .dialogBackground()
        ) {
            Text(
                text = stringResource(id = errorCode.toStringRes()),
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

@Preview(
    name = "Home Content - Portrait mode",
    device = Devices.PIXEL_2
)
@Preview(
    name = "Home Content - Landscape mode",
    widthDp = 732,
    heightDp = 412
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
            onDifficultyDialogConfirm = { _, _ -> },
            onContinueDialogConfirm = {},
            onDismissContinueDialog = {},
            onDismissDifficultyDialog = {},
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
