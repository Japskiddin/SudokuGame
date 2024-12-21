package io.github.japskiddin.sudoku.feature.history.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.ui.component.AppBar
import io.github.japskiddin.sudoku.core.ui.utils.toFormattedTime
import io.github.japskiddin.sudoku.feature.history.ui.components.GamePreview
import io.github.japskiddin.sudoku.feature.history.ui.logic.HistoryUI
import io.github.japskiddin.sudoku.feature.history.ui.logic.HistoryViewModel
import io.github.japskiddin.sudoku.feature.history.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.history.ui.logic.UiAction.Back
import io.github.japskiddin.sudoku.feature.history.ui.utils.toFormattedDate
import io.github.japskiddin.sudoku.feature.history.ui.utils.toFormattedString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
public fun HistoryScreen() {
    HistoryScreen(viewModel = hiltViewModel())
}

@Composable
private fun HistoryScreen(viewModel: HistoryViewModel) {
    BackHandler {
        viewModel.onAction(UiAction.Back)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryContent(
        history = uiState.history,
        onBack = { viewModel.onAction(Back) }
    )
}

@Composable
private fun HistoryContent(
    history: ImmutableList<HistoryUI>,
    onBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SudokuTheme.colors.primary)
    ) {
        AppBar(
            title = stringResource(id = CoreUiR.string.history),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .displayCutoutPadding()
                .padding(12.dp),
            onBack = onBack,
        )
        Spacer(modifier = Modifier.height(6.dp))
        HistoryList(
            history = history,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun HistoryList(
    history: ImmutableList<HistoryUI>,
    modifier: Modifier = Modifier,
) {
    val orientation = LocalConfiguration.current.orientation
    LazyColumn(
        modifier = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            modifier.displayCutoutPadding()
        } else {
            modifier
        },
    ) {
        items(
            items = history,
            key = { it.uid },
        ) { history ->
            HistoryItem(
                item = history,
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
            )
        }
        item {
            Spacer(
                Modifier.windowInsetsBottomHeight(
                    WindowInsets.systemBars
                )
            )
        }
    }
}

@Composable
private fun HistoryItem(
    item: HistoryUI,
    modifier: Modifier = Modifier,
) {
    val textStyle = SudokuTheme.typography.history.copy(
        color = SudokuTheme.colors.onCard,
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SudokuTheme.colors.card,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        BasicText(
            text = item.time.toFormattedDate(),
            style = textStyle.copy(
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(modifier = Modifier.height(6.dp))
        BasicText(
            text = stringResource(
                id = CoreUiR.string.status,
                item.status.toFormattedString(),
            ),
            style = textStyle,
        )
        Spacer(modifier = Modifier.height(6.dp))
        BasicText(
            text = stringResource(
                id = CoreUiR.string.started_at,
                item.startedAt.toFormattedDate(),
            ),
            style = textStyle,
        )
        Spacer(modifier = Modifier.height(6.dp))
        BasicText(
            text = stringResource(
                id = CoreUiR.string.finished_at,
                item.finishedAt.toFormattedDate(),
            ),
            style = textStyle,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GamePreview(
                board = item.board,
                size = item.type.size,
                modifier = Modifier.size(130.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                BasicText(
                    text = stringResource(
                        id = CoreUiR.string.play_time,
                        item.playTime.toFormattedTime(),
                    ),
                    style = textStyle,
                )
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = stringResource(
                        id = CoreUiR.string.current_actions,
                        item.actions,
                    ),
                    style = textStyle,
                )
                Spacer(modifier = Modifier.height(6.dp))
                BasicText(
                    text = stringResource(
                        id = CoreUiR.string.current_mistakes,
                        item.mistakes,
                        item.difficulty.mistakesLimit
                    ),
                    style = textStyle,
                )
            }
        }
    }
}

@Preview(
    name = "History Content - Portrait mode",
    device = Devices.PIXEL_2,
)
@Preview(
    name = "History Content - Landscape mode",
    widthDp = 732,
    heightDp = 412,
)
@Composable
private fun HistoryContentPreview() {
    val history: ImmutableList<HistoryUI> = persistentListOf(
        HistoryUI(
            uid = 1L,
            time = 1000221L,
            board = "",
            difficulty = GameDifficulty.INTERMEDIATE,
            type = GameType.DEFAULT9X9,
            actions = 1,
            mistakes = 3,
            playTime = 10000L,
            lastPlayed = 1000221L,
            startedAt = 100131L,
            finishedAt = 1000221L,
            status = GameStatus.COMPLETED,
        )
    )

    SudokuTheme {
        HistoryContent(
            history = history,
        )
    }
}
