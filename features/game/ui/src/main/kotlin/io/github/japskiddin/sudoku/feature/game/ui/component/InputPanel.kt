package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.core.model.Board
import io.github.japskiddin.sudoku.core.model.BoardCell
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun InputPanel(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    board: ImmutableList<ImmutableList<BoardCell>>
) {
    @Suppress("MagicNumber")
    val gameType: GameType = when (board.size) {
        6 -> GameType.DEFAULT6X6
        9 -> GameType.DEFAULT9X9
        12 -> GameType.DEFAULT12X12
        else -> GameType.UNSPECIFIED
    }
    @Suppress("MagicNumber")
    if (gameType.size > 9) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                @Suppress("MagicNumber")
                for (i in 1..9) {
                    InputButton(
                        onClick = { onClick(i) },
                        value = i.toString(),
                        counter = "0"
                    )
                }
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                @Suppress("MagicNumber")
                for (i in 10..12) {
                    InputButton(
                        onClick = { onClick(i) },
                        value = i.toString(),
                        counter = "0"
                    )
                }
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 1..gameType.size) {
                InputButton(
                    onClick = { onClick(i) },
                    value = i.toString(),
                    counter = "0"
                )
            }
        }
    }
}

@Composable
private fun InputButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    value: String,
    counter: String,
    valueTextSize: TextUnit = 16.sp,
    counterTextSize: TextUnit = 10.sp
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = valueTextSize
        )
        Text(
            text = counter,
            fontSize = counterTextSize
        )
    }
}

@Preview(
    name = "Input Panel",
    showBackground = true
)
@Composable
private fun InputPanelPreview(
    @PreviewParameter(InputPanelPreviewProvider::class) board: ImmutableList<ImmutableList<BoardCell>>
) {
    InputPanel(
        board = board,
        onClick = {}
    )
}

@Preview(
    name = "Input Button",
    showBackground = true
)
@Composable
private fun InputButtonPreview() {
    SudokuTheme {
        InputButton(
            onClick = {},
            value = "2",
            counter = "3"
        )
    }
}

private class InputPanelPreviewProvider : PreviewParameterProvider<ImmutableList<ImmutableList<BoardCell>>> {
    private val parser = SudokuParser()
    private val board = Board(
        initialBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        solvedBoard = "413004789741303043187031208703146980548700456478841230860200004894300064701187050",
        difficulty = GameDifficulty.INTERMEDIATE,
        type = GameType.DEFAULT9X9
    )
    private val parsedBoard = parser.parseBoard(
        board = board.initialBoard,
        gameType = board.type
    ).map { item -> item.toImmutableList() }
        .toImmutableList()

    override val values: Sequence<ImmutableList<ImmutableList<BoardCell>>>
        get() = sequenceOf(parsedBoard)
}
