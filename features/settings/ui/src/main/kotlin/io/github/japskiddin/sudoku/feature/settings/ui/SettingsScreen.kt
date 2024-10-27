package io.github.japskiddin.sudoku.feature.settings.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.settings.ui.logic.SettingsViewModel
import io.github.japskiddin.sudoku.feature.settings.ui.logic.UiState

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
    val screenModifier = Modifier
        .fillMaxSize()
        .then(modifier)
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        SettingsContentLandscape(modifier = screenModifier)
    } else {
        SettingsContentPortrait(modifier = screenModifier)
    }
}

@Composable
private fun SettingsContentPortrait(
    modifier: Modifier = Modifier
) {
}

@Composable
private fun SettingsContentLandscape(
    modifier: Modifier = Modifier
) {
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
