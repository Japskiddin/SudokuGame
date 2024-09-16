package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.OnPrimary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.game.ui.R

@Composable
internal fun ToolPanel(
    modifier: Modifier = Modifier,
    onEraserClick: () -> Unit,
    onNoteClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ToolButton(
            modifier = Modifier.weight(1f),
            onClick = onEraserClick,
            text = stringResource(id = R.string.tool_eraser),
            icon = painterResource(id = R.drawable.ic_tool_eraser)
        )
        ToolButton(
            modifier = Modifier.weight(1f),
            onClick = onUndoClick,
            text = stringResource(id = R.string.tool_undo),
            icon = painterResource(id = R.drawable.ic_tool_undo)
        )
        ToolButton(
            modifier = Modifier.weight(1f),
            onClick = onRedoClick,
            text = stringResource(id = R.string.tool_redo),
            icon = painterResource(id = R.drawable.ic_tool_redo)
        )
        ToolButton(
            modifier = Modifier.weight(1f),
            onClick = onNoteClick,
            text = stringResource(id = R.string.tool_note),
            icon = painterResource(id = R.drawable.ic_tool_note)
        )
        ToolButton(
            modifier = Modifier.weight(1f),
            onClick = onResetClick,
            text = stringResource(id = R.string.tool_reset),
            icon = painterResource(id = R.drawable.ic_tool_reset)
        )
    }
}

@Composable
private fun ToolButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    icon: Painter,
    iconSize: Dp = 36.dp,
    textSize: TextUnit = 16.sp,
    textColor: Color = OnPrimary
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = textColor,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = text,
            fontSize = textSize,
            color = textColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(
    name = "Tool Panel",
    showBackground = true,
    backgroundColor = 0xFFFAA468
)
@Composable
private fun ToolPanelPreview() {
    SudokuTheme {
        ToolPanel(
            onEraserClick = {},
            onNoteClick = {},
            onUndoClick = {},
            onRedoClick = {},
            onResetClick = {}
        )
    }
}
