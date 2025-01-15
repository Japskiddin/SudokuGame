package io.github.japskiddin.sudoku.feature.settings.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.AppBar
import io.github.japskiddin.sudoku.core.ui.component.HorizontalDivider
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape
import io.github.japskiddin.sudoku.feature.settings.ui.components.CheckableSettingsItem
import io.github.japskiddin.sudoku.feature.settings.ui.logic.SettingsViewModel
import io.github.japskiddin.sudoku.feature.settings.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.settings.ui.logic.UiState
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
public fun SettingsScreen() {
    SettingsScreen(viewModel = hiltViewModel())
}

@Composable
private fun SettingsScreen(viewModel: SettingsViewModel) {
    BackHandler {
        viewModel.onAction(UiAction.Back)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onUpdateMistakesLimit = { checked ->
            viewModel.onAction(UiAction.UpdateMistakesLimit(checked))
        },
        onUpdateShowTimer = { checked ->
            viewModel.onAction(UiAction.UpdateShowTimer(checked))
        },
        onUpdateResetTimer = { checked ->
            viewModel.onAction(UiAction.UpdateResetTimer(checked))
        },
        onUpdateHighlightErrorCells = { checked ->
            viewModel.onAction(UiAction.UpdateHighlightErrorCells(checked))
        },
        onUpdateHighlightSimilarCells = { checked ->
            viewModel.onAction(UiAction.UpdateHighlightSimilarCells(checked))
        },
        onUpdateShowRemainingNumbers = { checked ->
            viewModel.onAction(UiAction.UpdateShowRemainingNumbers(checked))
        },
        onUpdateHighlightSelectedCell = { checked ->
            viewModel.onAction(UiAction.UpdateHighlightSelectedCell(checked))
        },
        onUpdateKeepScreenOn = { checked ->
            viewModel.onAction(UiAction.UpdateKeepScreenOn(checked))
        },
        onUpdateSaveGameMode = { checked ->
            viewModel.onAction(UiAction.UpdateSaveGameMode(checked))
        },
        onBack = { viewModel.onAction(UiAction.Back) }
    )
}

@Composable
private fun SettingsContent(
    state: UiState,
    onUpdateMistakesLimit: (Boolean) -> Unit,
    onUpdateShowTimer: (Boolean) -> Unit,
    onUpdateResetTimer: (Boolean) -> Unit,
    onUpdateHighlightErrorCells: (Boolean) -> Unit,
    onUpdateHighlightSimilarCells: (Boolean) -> Unit,
    onUpdateShowRemainingNumbers: (Boolean) -> Unit,
    onUpdateHighlightSelectedCell: (Boolean) -> Unit,
    onUpdateKeepScreenOn: (Boolean) -> Unit,
    onUpdateSaveGameMode: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SudokuTheme.colors.primary)
    ) {
        AppBar(
            title = stringResource(id = CoreUiR.string.settings),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .displayCutoutPadding()
                .padding(12.dp),
            onBack = onBack,
        )

        val scrollState = rememberScrollState()
        val insetsModifier = if (isLandscape()) {
            Modifier
                .navigationBarsPadding()
                .displayCutoutPadding()
        } else {
            Modifier.navigationBarsPadding()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .then(insetsModifier)
                .padding(12.dp)
        ) {
            SectionGeneral(
                isKeepScreenOn = state.isKeepScreenOn,
                isSaveGameMode = state.isSaveGameMode,
                onUpdateKeepScreenOn = onUpdateKeepScreenOn,
                onUpdateSaveGameMode = onUpdateSaveGameMode
            )
            Spacer(modifier = Modifier.height(12.dp))
            SectionGame(
                isMistakesLimit = state.isMistakesLimit,
                isShowTimer = state.isShowTimer,
                isResetTimer = state.isResetTimer,
                isHighlightErrorCells = state.isHighlightErrorCells,
                isHighlightSimilarCells = state.isHighlightSimilarCells,
                isShowRemainingNumbers = state.isShowRemainingNumbers,
                isHighlightSelectedCell = state.isHighlightSelectedCell,
                onUpdateMistakesLimit = onUpdateMistakesLimit,
                onUpdateShowTimer = onUpdateShowTimer,
                onUpdateResetTimer = onUpdateResetTimer,
                onUpdateHighlightErrorCells = onUpdateHighlightErrorCells,
                onUpdateHighlightSimilarCells = onUpdateHighlightSimilarCells,
                onUpdateShowRemainingNumbers = onUpdateShowRemainingNumbers,
                onUpdateHighlightSelectedCell = onUpdateHighlightSelectedCell,
            )
        }
    }
}

@Composable
private fun SectionGeneral(
    isKeepScreenOn: Boolean,
    isSaveGameMode: Boolean,
    onUpdateKeepScreenOn: (Boolean) -> Unit,
    onUpdateSaveGameMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    TitleSection(
        title = stringResource(id = CoreUiR.string.settings_section_general),
        modifier = modifier,
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.keep_screen_on),
        description = stringResource(id = CoreUiR.string.keep_screen_on_desc),
        onCheckedChange = onUpdateKeepScreenOn,
        checked = isKeepScreenOn
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.save_last_game_mode),
        description = stringResource(id = CoreUiR.string.save_last_game_mode_desc),
        onCheckedChange = onUpdateSaveGameMode,
        checked = isSaveGameMode
    )
}

@Suppress("LongParameterList")
@Composable
private fun SectionGame(
    isMistakesLimit: Boolean,
    isShowTimer: Boolean,
    isResetTimer: Boolean,
    isHighlightErrorCells: Boolean,
    isHighlightSimilarCells: Boolean,
    isShowRemainingNumbers: Boolean,
    isHighlightSelectedCell: Boolean,
    onUpdateMistakesLimit: (Boolean) -> Unit,
    onUpdateShowTimer: (Boolean) -> Unit,
    onUpdateResetTimer: (Boolean) -> Unit,
    onUpdateHighlightErrorCells: (Boolean) -> Unit,
    onUpdateHighlightSimilarCells: (Boolean) -> Unit,
    onUpdateShowRemainingNumbers: (Boolean) -> Unit,
    onUpdateHighlightSelectedCell: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    TitleSection(
        title = stringResource(id = CoreUiR.string.settings_section_game),
        modifier = modifier,
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.mistakes_limit),
        description = stringResource(id = CoreUiR.string.mistakes_limit_desc),
        onCheckedChange = onUpdateMistakesLimit,
        checked = isMistakesLimit
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.show_timer),
        onCheckedChange = onUpdateShowTimer,
        checked = isShowTimer
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.reset_timer),
        description = stringResource(id = CoreUiR.string.reset_timer_desc),
        onCheckedChange = onUpdateResetTimer,
        checked = isResetTimer
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.highlight_cells_with_errors),
        onCheckedChange = onUpdateHighlightErrorCells,
        checked = isHighlightErrorCells
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.highlight_similar_cells),
        onCheckedChange = onUpdateHighlightSimilarCells,
        checked = isHighlightSimilarCells
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.show_remaining_numbers),
        description = stringResource(id = CoreUiR.string.show_remaining_numbers_desc),
        onCheckedChange = onUpdateShowRemainingNumbers,
        checked = isShowRemainingNumbers
    )
    CheckableSettingsItem(
        title = stringResource(id = CoreUiR.string.highlight_selected_cell),
        description = stringResource(id = CoreUiR.string.highlight_selected_cell_desc),
        onCheckedChange = onUpdateHighlightSelectedCell,
        checked = isHighlightSelectedCell
    )
}

@Composable
private fun TitleSection(
    title: String,
    modifier: Modifier = Modifier
) {
    BasicText(
        modifier = modifier.padding(
            start = 12.dp,
            end = 12.dp
        ),
        text = title,
        style = SudokuTheme.typography.dialog.copy(
            color = SudokuTheme.colors.onPrimary,
        ),
    )
    Spacer(modifier = modifier.height(6.dp))
    HorizontalDivider(
        modifier = modifier.padding(
            start = 12.dp,
            end = 12.dp
        ),
        color = SudokuTheme.colors.onPrimary.copy(alpha = 0.6f)
    )
    Spacer(modifier = modifier.height(6.dp))
}

@Preview(
    name = "Settings Content - Portrait mode",
    device = Devices.PIXEL_2,
)
@Preview(
    name = "Settings Content - Landscape mode",
    widthDp = 732,
    heightDp = 412,
)
@Composable
private fun SettingsContentPreview(
    @PreviewParameter(SettingsStateProvider::class) state: UiState
) {
    SudokuTheme {
        SettingsContent(
            state = state,
            onUpdateMistakesLimit = {},
            onUpdateShowTimer = {},
            onUpdateResetTimer = {},
            onUpdateHighlightErrorCells = {},
            onUpdateHighlightSimilarCells = {},
            onUpdateKeepScreenOn = {},
            onUpdateShowRemainingNumbers = {},
            onUpdateHighlightSelectedCell = {},
            onUpdateSaveGameMode = {},
            onBack = {},
        )
    }
}

private class SettingsStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Initial
        )
}
