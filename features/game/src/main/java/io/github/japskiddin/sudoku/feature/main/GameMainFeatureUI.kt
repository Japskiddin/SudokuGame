package io.github.japskiddin.sudoku.feature.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.japskiddin.sudoku.data.models.Difficulty
import io.github.japskiddin.sudoku.data.models.GameLevel

@Composable
fun GameScreen() {
    GameScreen(viewModel = hiltViewModel())
}

@Composable
internal fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.state.collectAsState()
    when (val currentState = state) {
        is State.Success -> Game(gameLevel = currentState.gameLevel)
        is State.Error -> Error(message = currentState.errorMessage)
        is State.Loading -> Loading()
        State.None -> Empty()
    }
}

@Composable
internal fun Game(
    gameLevel: GameLevel,
) {
    GameBoard(gameLevel = gameLevel)
}

@Composable
internal fun GameBoard(
    gameLevel: GameLevel,
) {
    Log.d("TEST", "GameBoard")
    val board = gameLevel.board
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            for (i in 0..8) {
                Row {
                    for (j in 0..8) {
                        Cell(
                            value = board[i][j], modifier = Modifier
                                .aspectRatio(1f)
                                .weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun Cell(
    value: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = Color.White)
            .border(width = 1.dp, color = Color.Black)
            .then(modifier),
    ) {
        Text(
            text = if (value == 0) {
                ""
            } else {
                value.toString()
            },
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
internal fun Loading() {
    Log.d("TEST", "Loading")
    Text(text = "Loading")
}

@Composable
internal fun Error(message: String) {
    Log.d("TEST", "Error")
}

@Composable
internal fun Empty() {
}

@Preview(
    name = "Game Board",
)
@Composable
internal fun GameBoardPreview(
    @PreviewParameter(GameLevelPreviewProvider::class) gameLevel: GameLevel
) {
    GameBoard(gameLevel)
}

@Preview(
    name = "Game Cell",
)
@Composable
internal fun CellPreview() {
    Cell(value = 1)
}

private class GameLevelPreviewProvider : PreviewParameterProvider<GameLevel> {
    private val board = Array(9) { IntArray(9) }.apply {
        for (i in 0..<9) {
            for (j in 0..<9) {
                this[i][j] = 1
            }
        }
    }

    override val values: Sequence<GameLevel>
        get() = sequenceOf(
            GameLevel(
                time = 0L,
                board = board,
                actions = 0,
                difficulty = Difficulty.NORMAL,
            ),
        )
}