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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.feature.utils.getName
import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.persistentListOf
import io.github.japskiddin.sudoku.core.ui.R as CoreUiR

@Composable
internal fun ItemSelector(
    currentItem: String,
    itemPos: Int,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 16.sp,
    buttonSize: Dp = 36.dp,
    swipeDuration: Int = 220,
    textColor: Color = SudokuTheme.colors.onDialog,
    iconTint: Color = SudokuTheme.colors.onDialog,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_left),
            contentDescription = stringResource(id = CoreUiR.string.swipe_left),
            modifier = Modifier
                .size(buttonSize)
                .clickable { onSwipeLeft() },
            colorFilter = ColorFilter.tint(color = iconTint)
        )
        AnimatedContent(
            targetState = itemPos,
            transitionSpec = {
                if (initialState >= targetState) {
                    slideInHorizontally(animationSpec = tween(swipeDuration)) { -it } togetherWith
                        slideOutHorizontally(animationSpec = tween(swipeDuration)) { it }
                } else {
                    slideInHorizontally(animationSpec = tween(swipeDuration)) { it } togetherWith
                        slideOutHorizontally(animationSpec = tween(swipeDuration)) { -it }
                }
            },
            label = "Animated Text",
            modifier = Modifier.weight(1f)
        ) { value: Int ->
            BasicText(
                text = currentItem,
                style = SudokuTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = textSize,
                    textAlign = TextAlign.Center,
                    color = textColor
                ),
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = stringResource(id = CoreUiR.string.swipe_right),
            modifier = Modifier
                .size(buttonSize)
                .clickable { onSwipeRight() },
            colorFilter = ColorFilter.tint(color = iconTint)
        )
    }
}

@Preview(
    name = "Item Selector",
    showBackground = true,
)
@Composable
private fun ItemSelectorPreview() {
    val difficulties = persistentListOf(
        GameDifficulty.EASY,
        GameDifficulty.INTERMEDIATE,
        GameDifficulty.HARD,
        GameDifficulty.EXPERT
    )
    var currentItemPos by remember { mutableIntStateOf(1) }
    val currentItem = difficulties[currentItemPos]

    SudokuTheme {
        ItemSelector(
            currentItem = stringResource(id = currentItem.getName()),
            itemPos = difficulties.indexOf(currentItem),
            onSwipeLeft = {
                if (currentItemPos <= 0) {
                    currentItemPos = difficulties.count() - 1
                } else {
                    currentItemPos--
                }
            },
            onSwipeRight = {
                if (currentItemPos >= difficulties.count() - 1) {
                    currentItemPos = 0
                } else {
                    currentItemPos++
                }
            }
        )
    }
}
