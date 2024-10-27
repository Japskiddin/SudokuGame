package io.github.japskiddin.sudoku.feature.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalConfiguration
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
import io.github.japskiddin.sudoku.feature.settings.ui.logic.SettingsViewModel
import io.github.japskiddin.sudoku.feature.settings.ui.logic.UiState
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
public fun SettingsScreen(modifier: Modifier = Modifier) {
    SettingsScreen(
        modifier = modifier,
        viewModel = hiltViewModel()
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsContent(
        modifier = modifier,
        state = state
    )
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    state: UiState
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
            .background(Primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding()
                .padding(12.dp),
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

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .safeDrawingPadding()
                .padding(12.dp)
        ) { }
    }
}

@Preview(
    name = "Settings Content - Portrait mode",
    device = Devices.PIXEL_2
)
@Preview(
    name = "Settings Content - Landscape mode",
    widthDp = 732,
    heightDp = 412
)
@Composable
private fun SettingsContentPreview(
    @PreviewParameter(SettingsStateProvider::class) state: UiState
) {
    SudokuTheme {
        SettingsContent(
            state = state
        )
    }
}

private class SettingsStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Initial
        )
}
