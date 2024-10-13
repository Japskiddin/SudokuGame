package io.github.japskiddin.sudoku.feature.game.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.OnPrimary
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.game.ui.R
import io.github.japskiddin.sudoku.feature.game.ui.utils.ToolAction
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun ToolPanel(
    modifier: Modifier = Modifier,
    onToolClick: (ToolAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ToolButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.tool_eraser),
            icon = painterResource(id = R.drawable.ic_tool_eraser)
        ) { onToolClick(ToolAction.ERASER) }
        ToolButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.tool_undo),
            icon = painterResource(id = R.drawable.ic_tool_undo)
        ) { onToolClick(ToolAction.UNDO) }
        ToolButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.tool_redo),
            icon = painterResource(id = R.drawable.ic_tool_redo)
        ) { onToolClick(ToolAction.REDO) }
        ToolButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.tool_note),
            icon = painterResource(id = R.drawable.ic_tool_note)
        ) { onToolClick(ToolAction.NOTE) }
        ToolButton(
            modifier = Modifier.weight(1f),
            text = stringResource(id = CoreUiR.string.tool_reset),
            icon = painterResource(id = R.drawable.ic_tool_reset)
        ) { onToolClick(ToolAction.RESET) }
    }
}

@Composable
private fun ToolButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    iconSize: Dp = 36.dp,
    textSize: TextUnit = 16.sp,
    textColor: Color = OnPrimary,
    onClick: () -> Unit
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
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = textSize,
            color = textColor
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
            onToolClick = {}
        )
    }
}
