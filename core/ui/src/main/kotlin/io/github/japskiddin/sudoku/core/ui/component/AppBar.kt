package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.R

@Composable
public fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    Row(
        modifier = modifier.padding(
            start = 12.dp,
            end = 12.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlineText(
            modifier = Modifier
                .weight(1f)
                .padding(start = 40.dp, end = 4.dp),
            text = title,
            textStyle = SudokuTheme.typography.appBar
        )
        Image(
            modifier = Modifier
                .clip(CircleShape)
                .size(36.dp)
                .clickable { onBack() },
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = stringResource(id = R.string.close)
        )
    }
}
