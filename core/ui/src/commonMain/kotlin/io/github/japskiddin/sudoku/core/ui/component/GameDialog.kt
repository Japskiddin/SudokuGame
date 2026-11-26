package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import io.github.japskiddin.sudoku.core.ui.utils.dialogBackground
import io.github.japskiddin.sudoku.core.ui.utils.isLandscape

private const val DialogWidthPercentLandscape: Float = .6f
private const val DialogWidthPercentPortrait: Float = .8f
private const val SlideOutOffsetY: Int = 8

@Composable
public fun GameDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val visibility = updateTransition(targetState = showDialog, label = "Dialog visibility")

    if (showDialog || visibility.currentState) {
        GameDialogWindow(onDismiss = onDismiss) {
            GameDialogContent(visibility = visibility, onDismiss = onDismiss, content = content)
        }
    }
}

@Composable
@Suppress("LongMethod")
private fun GameDialogContent(
    visibility: Transition<Boolean>,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismiss by rememberUpdatedState(onDismiss)
    val contentWidth = if (isLandscape()) {
        DialogWidthPercentLandscape
    } else {
        DialogWidthPercentPortrait
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        visibility.AnimatedVisibility(
            visible = { it },
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = .5f))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            dismiss()
                        }
                    }
            )
        }
        visibility.AnimatedVisibility(
            visible = { it },
            enter = fadeIn(spring(stiffness = Spring.StiffnessHigh)) + scaleIn(
                initialScale = .8f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
            exit = slideOutVertically { it / SlideOutOffsetY } + fadeOut() + scaleOut(targetScale = .95f)
        ) {
            Box(
                Modifier
                    .fillMaxWidth(contentWidth)
                    .dialogBackground()
                    .pointerInput(Unit) {
                        detectTapGestures { }
                    },
                contentAlignment = Alignment.Center
            ) {
                var graphicVisible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) { graphicVisible = true }

                AnimatedVisibility(
                    visible = graphicVisible,
                    enter = expandVertically(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        expandFrom = Alignment.CenterVertically,
                    )
                ) {
                    content()
                }
            }
        }
    }
}

@Preview(
    name = "Game Dialog",
    showBackground = true,
)
@Composable
private fun GameDialogPreview() {
    SudokuTheme {
        GameDialog(
            showDialog = true,
            onDismiss = {}
        ) {
            BasicText(text = "This is dialog")
        }
    }
}
