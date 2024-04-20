package io.github.japskiddin.sudoku.feature.main

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.japskiddin.sudoku.data.models.Difficulty

private const val TAG = "Game UI"

@Composable
fun GameScreen() {
    GameScreen(viewModel = hiltViewModel())
}

@Composable
internal fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()
    when (val currentState = state) {
        is UiState.Success -> Game(gameLevelUi = currentState.gameLevelUi)
        is UiState.Error -> Error(message = currentState.message)
        is UiState.Loading -> Loading()
        UiState.None -> Empty()
    }
}

@Composable
internal fun Game(
    gameLevelUi: GameLevelUi,
) {
    if (BuildConfig.DEBUG) Log.d(TAG, "Composing Game screen")
    var selectedCell: Pair<Int, Int> = remember { Pair(-1, -1) }
    GameBoard(
        gameLevelUi = gameLevelUi,
        selectedCell = selectedCell,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    )
}

@Composable
internal fun GameBoard(
    modifier: Modifier = Modifier,
    gameLevelUi: GameLevelUi,
    selectedCell: Pair<Int, Int> = Pair(-1, -1),
) {
    val board = gameLevelUi.board
    val size = board.size
    val divider = if (size >= 6) {
        size / 3
    } else {
        0
    }
    Box(
        modifier = modifier.border(width = 1f.dp, color = Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in board.indices) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min)
                ) {
                    val cells = board[i]
                    for (j in cells.indices) {
                        Cell(
                            value = board[i][j],
                            isSelected = selectedCell.first == i && selectedCell.second == j,
                            onClick = {
//                                onSelectCell(i, j)
                            },
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
                        )
                        if (divider != 0 && ((j + 1) % divider == 0)) {
                            VerticalDivider(
                                color = Color.Black,
                                thickness = 1.dp,
                                modifier = Modifier.fillMaxHeight()
                            )
                        }
                    }
                }
                if (divider != 0 && ((i + 1) % divider == 0)) {
                    HorizontalDivider(
                        color = Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
internal fun Cell(
    value: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                color = if (isSelected) {
                    Color.Green
                } else {
                    Color.White
                }
            )
            .border(width = .1f.dp, color = Color.Black.copy(alpha = .7f))
            .then(modifier)
            .clickable { onClick() },
    ) {
        Text(
            text = if (value == 0) {
                ""
            } else {
                value.toString()
            },
            color = if (isSelected) {
                Color.White
            } else {
                Color.Black
            },
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
internal fun Loading() {
    if (BuildConfig.DEBUG) Log.d(TAG, "Composing Loading screen")
    Text(text = "Loading")
}

@Composable
internal fun Error(@StringRes message: Int) {
    if (BuildConfig.DEBUG) Log.d(TAG, "Composing Error screen")
    Text(text = stringResource(id = message))
}

@Composable
internal fun Empty() {
    if (BuildConfig.DEBUG) Log.d(TAG, "Composing Empty screen")
}

@Preview(
    name = "Game Preview",
)
@Composable
internal fun GamePreview(
    @PreviewParameter(GameLevelUiPreviewProvider::class) gameLevelUi: GameLevelUi
) {
    Game(gameLevelUi)
}

@Preview(
    name = "Game Cell",
)
@Composable
internal fun CellPreview() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Cell(value = 1, isSelected = false, onClick = {})
        Cell(value = 2, isSelected = true, onClick = {})
    }
}

private class GameLevelUiPreviewProvider : PreviewParameterProvider<GameLevelUi> {
    private val generator = SudokuGenerator(9, 50).apply {
        generate()
    }
    private val board = generator.getResult().items
    private val completedBoard = generator.getResult().completedItems

    override val values: Sequence<GameLevelUi>
        get() = sequenceOf(
            GameLevelUi(
                time = 0L,
                board = board,
                completedBoard = completedBoard,
                actions = 0,
                difficulty = Difficulty.NORMAL,
            ),
        )
}