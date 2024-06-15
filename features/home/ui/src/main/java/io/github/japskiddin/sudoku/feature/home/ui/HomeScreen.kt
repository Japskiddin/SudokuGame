package io.github.japskiddin.sudoku.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import io.github.japskiddin.sudoku.core.ui.component.Loading
import io.github.japskiddin.sudoku.core.ui.component.innerShadow
import io.github.japskiddin.sudoku.core.ui.theme.OnPrimary
import io.github.japskiddin.sudoku.core.ui.theme.Primary
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.home.domain.ErrorCode
import io.github.japskiddin.sudoku.feature.home.domain.HomeViewModel
import io.github.japskiddin.sudoku.feature.home.domain.UiState
import io.github.japskiddin.sudoku.feature.home.ui.components.Menu
import io.github.japskiddin.sudoku.feature.home.ui.components.OutlineText

@Composable
public fun HomeScreen(modifier: Modifier = Modifier) {
    HomeScreen(modifier = modifier, viewModel = hiltViewModel())
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
        onRecordsClick = { viewModel.onRecordsClick() }
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
    onSettingsClick: () -> Unit
) {
    val screenModifier = Modifier
        .fillMaxSize()
        .then(modifier)
    when (state) {
        is UiState.Menu ->
            MainMenu(
                modifier = screenModifier,
                currentYear = currentYear,
                isContinueAvailable = state.isContinueVisible,
                onStartGameClick = onStartGameClick,
                onContinueGameClick = onContinueGameClick,
                onRecordsClick = onRecordsClick,
                onSettingsClick = onSettingsClick
            )

        is UiState.Error ->
            HomeError(
                modifier = screenModifier,
                message = stringResource(
                    id = when (state.code) {
                        ErrorCode.SUDOKU_NOT_GENERATED -> R.string.err_generate_sudoku
                        else -> R.string.err_unknown
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
private fun MainMenu(
    modifier: Modifier = Modifier,
    widthPercent: Float = .8f,
    isContinueAvailable: Boolean,
    currentYear: String,
    onStartGameClick: () -> Unit,
    onContinueGameClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRecordsClick: () -> Unit
) {
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
                isContinueAvailable = isContinueAvailable,
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
        modifier =
        Modifier
            .then(modifier)
            .background(Primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .padding(16.dp)
                .background(
                    color = OnPrimary,
                    shape = RoundedCornerShape(size = 16.dp)
                )
                .padding(4.dp)
                .innerShadow(
                    shape = RoundedCornerShape(size = 12.dp),
                    color = Color.Black.copy(alpha = .8f),
                    offsetX = 2.dp,
                    offsetY = 2.dp
                )
                .innerShadow(
                    shape = RoundedCornerShape(size = 12.dp),
                    color = Color.White.copy(alpha = .8f),
                    offsetX = (-2).dp,
                    offsetY = (-2).dp
                )
                .padding(16.dp)
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
private fun MainContentPreview(
    @PreviewParameter(HomeStateProvider::class) state: UiState
) {
    SudokuTheme {
        HomeScreenContent(
            state = state,
            currentYear = "2024",
            onStartGameClick = {},
            onContinueGameClick = {},
            onRecordsClick = {},
            onSettingsClick = {}
        )
    }
}

private class HomeStateProvider : PreviewParameterProvider<UiState> {
    override val values: Sequence<UiState>
        get() = sequenceOf(
            UiState.Menu(),
            UiState.Loading,
            UiState.Error(code = ErrorCode.SUDOKU_NOT_GENERATED)
        )
}
