package io.github.japskiddin.sudoku.feature.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.component.OutlineText
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
        onUpdateSaveLastDifficulty = { checked ->
            viewModel.onAction(UiAction.UpdateSaveLastDifficulty(checked))
        }
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
    onUpdateSaveLastDifficulty: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SudokuTheme.colors.primary)
            .safeDrawingPadding()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlineText(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 40.dp, end = 4.dp),
                text = stringResource(id = CoreUiR.string.settings),
                textStyle = SudokuTheme.typography.titleMedium
            )
            Image(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = CoreUiR.drawable.ic_close),
                contentDescription = stringResource(id = CoreUiR.string.close)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            BasicText(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp
                ),
                text = stringResource(id = CoreUiR.string.settings_section_general),
                style = SudokuTheme.typography.bodyMedium.copy(
                    color = SudokuTheme.colors.onPrimary,
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            // TODO добавить описание в строках
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.keep_screen_on),
                onCheckedChange = onUpdateKeepScreenOn,
                checked = state.isKeepScreenOn
            )
            // TODO добавить описание в строках
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.save_last_game_difficulty_type),
                onCheckedChange = onUpdateSaveLastDifficulty,
                checked = state.isSaveLastDifficulty
            )
            Spacer(modifier = Modifier.height(12.dp))
            BasicText(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp
                ),
                text = stringResource(id = CoreUiR.string.settings_section_game),
                style = SudokuTheme.typography.bodyMedium.copy(
                    color = SudokuTheme.colors.onPrimary
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.mistakes_limit),
                description = stringResource(id = CoreUiR.string.mistakes_limit_desc),
                onCheckedChange = onUpdateMistakesLimit,
                checked = state.isMistakesLimit
            )
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.show_timer),
                onCheckedChange = onUpdateShowTimer,
                checked = state.isShowTimer
            )
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.reset_timer),
                description = stringResource(id = CoreUiR.string.reset_timer_desc),
                onCheckedChange = onUpdateResetTimer,
                checked = state.isResetTimer
            )
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.highlight_cells_with_errors),
                onCheckedChange = onUpdateHighlightErrorCells,
                checked = state.isHighlightErrorCells
            )
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.highlight_similar_cells),
                onCheckedChange = onUpdateHighlightSimilarCells,
                checked = state.isHighlightSimilarCells
            )
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.show_remaining_numbers),
                description = stringResource(id = CoreUiR.string.show_remaining_numbers_desc),
                onCheckedChange = onUpdateShowRemainingNumbers,
                checked = state.isShowRemainingNumbers
            )
            // TODO добавить описание в строках
            CheckableSettingsItem(
                title = stringResource(id = CoreUiR.string.highlight_selected_cell),
                onCheckedChange = onUpdateHighlightSelectedCell,
                checked = state.isHighlightSelectedCell
            )
        }
    }
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
            onUpdateSaveLastDifficulty = {},
        )
    }
}

private class SettingsStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Initial
        )
}
