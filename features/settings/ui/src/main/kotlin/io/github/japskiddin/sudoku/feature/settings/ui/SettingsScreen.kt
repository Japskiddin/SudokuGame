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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import io.github.japskiddin.sudoku.core.designsystem.theme.Primary
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
        }
    )
}

@Composable
private fun SettingsContent(
    state: UiState,
    onUpdateMistakesLimit: (Boolean) -> Unit,
    onUpdateShowTimer: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
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
                textStyle = MaterialTheme.typography.titleMedium
            )
            Image(
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = CoreUiR.drawable.ic_close),
                contentDescription = stringResource(id = CoreUiR.string.close)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
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
            onUpdateShowTimer = {}
        )
    }
}

private class SettingsStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Initial
        )
}
