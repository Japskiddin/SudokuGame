package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ItemSelector(
    modifier: Modifier = Modifier,
    items: ImmutableList<String>,
    defaultItemPos: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = stringResource(id = R.string.swipe_left)
        )
        Text(
            text = "Text",
            modifier = Modifier.fillMaxWidth()
        )
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = stringResource(id = R.string.swipe_right)
        )
    }
}

@Preview(
    name = "Item Selector",
    showBackground = true
)
@Composable
private fun ItemSelectorPreview() {
    SudokuTheme {
        ItemSelector(
            items = persistentListOf("Item 1", "Item 2", "Item 3", "Item 4"),
            defaultItemPos = 1
        )
    }
}
