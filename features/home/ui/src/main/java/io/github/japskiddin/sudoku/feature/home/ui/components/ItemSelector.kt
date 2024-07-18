package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.ui.theme.SudokuTheme
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ItemSelector(
    modifier: Modifier = Modifier,
    items: ImmutableList<String>,
    defaultItemPos: Int,
    textSize: TextUnit = 14.sp,
    buttonSize: Dp = 24.dp,
    onSelectedItemChanged: (Int) -> Unit,
) {
    var currentItemPos by remember { mutableIntStateOf(defaultItemPos) }

    // TODO: добавить настройку цвета для текста и иконок

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = stringResource(id = R.string.swipe_left),
            modifier = Modifier
                .size(buttonSize)
                .clickable {
                    if (currentItemPos <= 0) {
                        currentItemPos = items.count() - 1
                    } else {
                        currentItemPos--
                    }
                    onSelectedItemChanged(currentItemPos)
                }
        )
        AnimatedContent(
            targetState = currentItemPos,
            transitionSpec = {
                val duration = 220
                if (initialState > targetState) {
                    slideInHorizontally(animationSpec = tween(duration)) { it } togetherWith
                        slideOutHorizontally(animationSpec = tween(duration)) { -it }
                } else {
                    slideInHorizontally(animationSpec = tween(duration)) { -it } togetherWith
                        slideOutHorizontally(animationSpec = tween(duration)) { it }
                }
            },
            label = "Animated Text",
            modifier = Modifier.weight(1f)
        ) { pos ->
            Text(
                text = items[pos],
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontSize = textSize
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = stringResource(id = R.string.swipe_right),
            modifier = Modifier
                .size(buttonSize)
                .clickable {
                    if (currentItemPos >= items.count() - 1) {
                        currentItemPos = 0
                    } else {
                        currentItemPos++
                    }
                    onSelectedItemChanged(currentItemPos)
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
            defaultItemPos = 1,
            onSelectedItemChanged = {},
        )
    }
}
