package io.github.japskiddin.sudoku.feature.history.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameStatus
import io.github.japskiddin.sudoku.core.model.GameType
import io.github.japskiddin.sudoku.core.model.toImmutable
import io.github.japskiddin.sudoku.core.ui.component.AppBar
import io.github.japskiddin.sudoku.core.ui.utils.SudokuPreview
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape
import io.github.japskiddin.sudoku.core.ui.utils.toFormattedTime
import io.github.japskiddin.sudoku.feature.history.ui.components.HistoryGameBoard
import io.github.japskiddin.sudoku.feature.history.ui.logic.HistoryUI
import io.github.japskiddin.sudoku.feature.history.ui.logic.HistoryViewModel
import io.github.japskiddin.sudoku.feature.history.ui.logic.UiAction
import io.github.japskiddin.sudoku.feature.history.ui.utils.cardBackground
import io.github.japskiddin.sudoku.feature.history.ui.utils.toFormattedDate
import io.github.japskiddin.sudoku.feature.history.ui.utils.toFormattedString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random
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
        onBack = { viewModel.onAction(UiAction.Back) }
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

        if (history.isNotEmpty()) {
            HistoryList(
                history = history,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            EmptyHistory(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun EmptyHistory(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = stringResource(id = CoreUiR.string.history_placeholder),
            style = SudokuTheme.typography.panel.copy(
                color = SudokuTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
            ),
        )
    }
}

@Composable
private fun HistoryList(
    history: ImmutableList<HistoryUI>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = if (isLandscape()) {
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

    if (isLandscape()) {
        LandscapeHistoryItem(
            item = item,
            textStyle = textStyle,
            modifier = modifier,
        )
    } else {
        PortraitHistoryItem(
            item = item,
            textStyle = textStyle,
            modifier = modifier,
        )
    }
}

@Composable
private fun LandscapeHistoryItem(
    item: HistoryUI,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .cardBackground()
    ) {
        HistoryGameBoard(
            board = item.board,
            size = item.type.size,
            modifier = Modifier.size(130.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Statistics(
            item = item,
            textStyle = textStyle,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        GameplayInformation(
            item = item,
            textStyle = textStyle,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PortraitHistoryItem(
    item: HistoryUI,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .cardBackground()
    ) {
        Statistics(
            item = item,
            textStyle = textStyle,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HistoryGameBoard(
                board = item.board,
                size = item.type.size,
                modifier = Modifier.size(130.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            GameplayInformation(
                item = item,
                textStyle = textStyle,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Statistics(
    item: HistoryUI,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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
    }
}

@Composable
private fun GameplayInformation(
    item: HistoryUI,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
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

@Suppress("UnusedPrivateMember")
@SudokuPreview
@Composable
private fun HistoryContentPreview() {
    @Suppress("MagicNumber")
    val boardSize = 9

    val history: ImmutableList<HistoryUI> = persistentListOf(
        HistoryUI(
            uid = 1L,
            time = 1000221L,
            board = List(boardSize) { row ->
                List(boardSize) { col ->
                    BoardCell(row, col, Random.nextInt(boardSize))
                }
            }.toImmutable(),
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
