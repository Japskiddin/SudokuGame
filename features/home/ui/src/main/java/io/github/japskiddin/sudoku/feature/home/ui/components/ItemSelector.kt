package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var currentItemPos by remember { mutableIntStateOf(defaultItemPos) }
    val currentItem = items[currentItemPos]

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = stringResource(id = R.string.swipe_left),
            modifier = Modifier.clickable {
                if (currentItemPos <= 0) {
                    currentItemPos = items.count() - 1
                } else {
                    currentItemPos--
                }
            }
        )
        Text(
            text = currentItem,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = stringResource(id = R.string.swipe_right),
            modifier = Modifier.clickable {
                if (currentItemPos >= items.count() - 1) {
                    currentItemPos = 0
                } else {
                    currentItemPos++
                }
            }
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
