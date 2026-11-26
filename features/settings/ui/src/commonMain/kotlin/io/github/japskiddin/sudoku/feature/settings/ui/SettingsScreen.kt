package io.github.japskiddin.sudoku.feature.settings.ui

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.zacsweers.metrox.viewmodel.metroViewModel
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.AppBar
import io.github.japskiddin.sudoku.core.ui.component.BackHandler
import io.github.japskiddin.sudoku.core.ui.component.HorizontalDivider
import io.github.japskiddin.sudoku.core.ui.resources.highlight_cells_with_errors
import io.github.japskiddin.sudoku.core.ui.resources.highlight_selected_cell
import io.github.japskiddin.sudoku.core.ui.resources.highlight_selected_cell_desc
import io.github.japskiddin.sudoku.core.ui.resources.highlight_similar_cells
import io.github.japskiddin.sudoku.core.ui.resources.keep_screen_on
import io.github.japskiddin.sudoku.core.ui.resources.keep_screen_on_desc
import io.github.japskiddin.sudoku.core.ui.resources.mistakes_limit
import io.github.japskiddin.sudoku.core.ui.resources.mistakes_limit_desc
import io.github.japskiddin.sudoku.core.ui.resources.reset_timer
import io.github.japskiddin.sudoku.core.ui.resources.reset_timer_desc
import io.github.japskiddin.sudoku.core.ui.resources.save_last_game_mode
import io.github.japskiddin.sudoku.core.ui.resources.save_last_game_mode_desc
import io.github.japskiddin.sudoku.core.ui.resources.settings
import io.github.japskiddin.sudoku.core.ui.resources.settings_section_game
import io.github.japskiddin.sudoku.core.ui.resources.settings_section_general
import io.github.japskiddin.sudoku.core.ui.resources.show_remaining_numbers
import io.github.japskiddin.sudoku.core.ui.resources.show_remaining_numbers_desc
import io.github.japskiddin.sudoku.core.ui.resources.show_timer
import io.github.japskiddin.sudoku.core.ui.utils.PreviewSudoku
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape
import io.github.japskiddin.sudoku.core.ui.utils.supportsKeepScreenOn
import io.github.japskiddin.sudoku.feature.settings.ui.components.CheckableSettingsItem
import io.github.japskiddin.sudoku.feature.settings.ui.logic.SettingsViewModel
import io.github.japskiddin.sudoku.feature.settings.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.settings.ui.logic.UiState
import org.jetbrains.compose.resources.stringResource
import io.github.japskiddin.sudoku.core.ui.resources.Res as CoreUiRes

@Composable
public fun SettingsScreen() {
    SettingsScreen(viewModel = metroViewModel())
}

@Composable
private fun SettingsScreen(viewModel: SettingsViewModel) {
    BackHandler {
        viewModel.onAction(UiAction.Back)
    }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SettingsContent(
    state: UiState,
    onAction: (UiAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SudokuTheme.colors.primary)
    ) {
        AppBar(
            title = stringResource(resource = CoreUiRes.string.settings),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .displayCutoutPadding()
                .padding(top = 12.dp, bottom = 12.dp),
            onBack = { onAction(UiAction.Back) },
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
                onUpdateKeepScreenOn = { onAction(UiAction.UpdateKeepScreenOn(it)) },
                onUpdateSaveGameMode = { onAction(UiAction.UpdateSaveGameMode(it)) },
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
                onUpdateMistakesLimit = { onAction(UiAction.UpdateMistakesLimit(it)) },
                onUpdateShowTimer = { onAction(UiAction.UpdateShowTimer(it)) },
                onUpdateResetTimer = { onAction(UiAction.UpdateResetTimer(it)) },
                onUpdateHighlightErrorCells = { onAction(UiAction.UpdateHighlightErrorCells(it)) },
                onUpdateHighlightSimilarCells = { onAction(UiAction.UpdateHighlightSimilarCells(it)) },
                onUpdateShowRemainingNumbers = { onAction(UiAction.UpdateShowRemainingNumbers(it)) },
                onUpdateHighlightSelectedCell = { onAction(UiAction.UpdateHighlightSelectedCell(it)) },
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
        title = stringResource(resource = CoreUiRes.string.settings_section_general),
        modifier = modifier,
    )
    if (supportsKeepScreenOn) {
        CheckableSettingsItem(
            title = stringResource(resource = CoreUiRes.string.keep_screen_on),
            description = stringResource(resource = CoreUiRes.string.keep_screen_on_desc),
            onCheckedChange = onUpdateKeepScreenOn,
            checked = isKeepScreenOn
        )
    }
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.save_last_game_mode),
        description = stringResource(resource = CoreUiRes.string.save_last_game_mode_desc),
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
        title = stringResource(resource = CoreUiRes.string.settings_section_game),
        modifier = modifier,
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.mistakes_limit),
        description = stringResource(resource = CoreUiRes.string.mistakes_limit_desc),
        onCheckedChange = onUpdateMistakesLimit,
        checked = isMistakesLimit
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.show_timer),
        onCheckedChange = onUpdateShowTimer,
        checked = isShowTimer
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.reset_timer),
        description = stringResource(resource = CoreUiRes.string.reset_timer_desc),
        onCheckedChange = onUpdateResetTimer,
        checked = isResetTimer
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.highlight_cells_with_errors),
        onCheckedChange = onUpdateHighlightErrorCells,
        checked = isHighlightErrorCells
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.highlight_similar_cells),
        onCheckedChange = onUpdateHighlightSimilarCells,
        checked = isHighlightSimilarCells
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.show_remaining_numbers),
        description = stringResource(resource = CoreUiRes.string.show_remaining_numbers_desc),
        onCheckedChange = onUpdateShowRemainingNumbers,
        checked = isShowRemainingNumbers
    )
    CheckableSettingsItem(
        title = stringResource(resource = CoreUiRes.string.highlight_selected_cell),
        description = stringResource(resource = CoreUiRes.string.highlight_selected_cell_desc),
        onCheckedChange = onUpdateHighlightSelectedCell,
        checked = isHighlightSelectedCell
    )
}

@Composable
private fun TitleSection(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        BasicText(
            text = title,
            style = SudokuTheme.typography.settingsSection.copy(
                color = SudokuTheme.colors.onPrimary,
            ),
        )
        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(color = SudokuTheme.colors.onPrimary.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@PreviewSudoku
@Composable
private fun SettingsContentPreview(
    @PreviewParameter(SettingsStateProvider::class) state: UiState
) {
    SudokuTheme {
        SettingsContent(
            state = state,
            onAction = {},
        )
    }
}

private class SettingsStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Initial
        )
}
