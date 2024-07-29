package io.github.japskiddin.sudoku.feature.home.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.game.GameDifficulty
import io.github.japskiddin.sudoku.feature.home.ui.R
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ItemSelector(
    modifier: Modifier = Modifier,
    currentItem: String,
    itemPos: Int,
    textSize: TextUnit = 16.sp,
    buttonSize: Dp = 36.dp,
    swipeDuration: Int = 220,
    textColor: Color = Color.Black,
    iconTint: Color = Color.Black,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    // TODO: добавить настройку цвета для текста и иконок

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onSwipeLeft() },
            modifier = Modifier.size(buttonSize)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                tint = iconTint,
                contentDescription = stringResource(id = R.string.swipe_left)
            )
        }
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
        ) { _ ->
            Text(
                text = currentItem,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = textColor,
                fontSize = textSize
            )
        }
        IconButton(
            onClick = { onSwipeRight() },
            modifier = Modifier.size(buttonSize)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                tint = iconTint,
                contentDescription = stringResource(id = R.string.swipe_right)
            )
        }
    }
}

@Preview(
    name = "Item Selector",
    showBackground = true
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
            currentItem = stringResource(id = currentItem.resName),
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
