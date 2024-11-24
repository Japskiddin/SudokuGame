package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import io.github.japskiddin.sudoku.core.designsystem.theme.SudokuTheme
import kotlin.math.abs

@Composable
public fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    indicatorColor: Color = SudokuTheme.colors.primary,
    trackColor: Color = indicatorColor.copy(alpha = 0.8f),
    strokeCap: StrokeCap = StrokeCap.Butt,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "progress")
    val firstLineHead by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at FirstLineHeadDelay using FirstLineHeadEasing
                1f at FirstLineHeadDuration + FirstLineHeadDelay
            }
        ),
        label = "first line head"
    )
    val firstLineTail by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at FirstLineTailDelay using FirstLineTailEasing
                1f at FirstLineTailDuration + FirstLineTailDelay
            }
        ),
        label = "first line tail"
    )
    val secondLineHead by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at SecondLineHeadDelay using SecondLineHeadEasing
                1f at SecondLineHeadDuration + SecondLineHeadDelay
            }
        ),
        label = "second line head"
    )
    val secondLineTail by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at SecondLineTailDelay using SecondLineTailEasing
                1f at SecondLineTailDuration + SecondLineTailDelay
            }
        ),
        label = "second line tail"
    )

    Canvas(
        modifier
            .increaseSemanticsBounds()
            .progressSemantics()
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
    ) {
        val strokeWidth = size.height
        drawLinearIndicatorBackground(trackColor, strokeWidth, strokeCap)
        if ((firstLineHead - firstLineTail) > 0) {
            drawLinearIndicator(
                firstLineHead,
                firstLineTail,
                indicatorColor,
                strokeWidth,
                strokeCap,
            )
        }
        if ((secondLineHead - secondLineTail) > 0) {
            drawLinearIndicator(
                secondLineHead,
                secondLineTail,
                indicatorColor,
                strokeWidth,
                strokeCap,
            )
        }
    }
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // if there isn't enough space to draw the stroke caps, fall back to StrokeCap.Butt
    if (strokeCap == StrokeCap.Butt || height > width) {
        // Progress line
        drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
    } else {
        // need to adjust barStart and barEnd for the stroke caps
        val strokeCapOffset = strokeWidth / 2
        val coerceRange = strokeCapOffset..(width - strokeCapOffset)
        val adjustedBarStart = barStart.coerceIn(coerceRange)
        val adjustedBarEnd = barEnd.coerceIn(coerceRange)

        if (abs(endFraction - startFraction) > 0) {
            // Progress line
            drawLine(
                color = color,
                start = Offset(adjustedBarStart, yOffset),
                end = Offset(adjustedBarEnd, yOffset),
                strokeWidth = strokeWidth,
                cap = strokeCap,
            )
        }
    }
}

private fun DrawScope.drawLinearIndicatorBackground(
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
) = drawLinearIndicator(
    startFraction = 0f,
    endFraction = 1f,
    color = color,
    strokeWidth = strokeWidth,
    strokeCap = strokeCap
)

private fun Modifier.increaseSemanticsBounds(padding: Dp = 10.dp): Modifier = this
    .layout { measurable, constraints ->
        val paddingPx = padding.roundToPx()
        // We need to add vertical padding to the semantics bounds in other to meet
        // screenreader green box minimum size, but we also want to
        // preserve a visual appearance and layout size below that minimum
        // in order to maintain backwards compatibility. This custom
        // layout effectively implements "negative padding".
        val newConstraint = constraints.offset(0, paddingPx * 2)
        val placeable = measurable.measure(newConstraint)

        // But when actually placing the placeable, create the layout without additional
        // space. Place the placeable where it would've been without any extra padding.
        val height = placeable.height - paddingPx * 2
        val width = placeable.width
        layout(width, height) {
            placeable.place(0, -paddingPx)
        }
    }
    .semantics(mergeDescendants = true) {}
    .padding(vertical = padding)

public object ProgressIndicatorDefaults {
    /**
     * Default  height for [LinearProgressIndicator].
     *
     * This can be customized with the `strokeWidth` parameter on [CircularProgressIndicator], and
     * by passing a layout modifier setting the height for [LinearProgressIndicator].
     */
    public val StrokeWidth: Dp = 4.dp

    /**
     * The default opacity applied to the indicator color to create the background color in a
     * [LinearProgressIndicator].
     */
    public const val IndicatorBackgroundOpacity: Float = 0.24f

    /**
     * The default [AnimationSpec] that should be used when animating between progress in a
     * determinate progress indicator.
     */
    public val ProgressAnimationSpec: SpringSpec<Float> = SpringSpec(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow,
        // The default threshold is 0.01, or 1% of the overall progress range, which is quite
        // large and noticeable.
        visibilityThreshold = 1 / 1000f
    )
}

// LinearProgressIndicator Material specs
private val LinearIndicatorHeight = ProgressIndicatorDefaults.StrokeWidth
private val LinearIndicatorWidth = 240.dp

// Indeterminate linear indicator transition specs
// Total duration for one cycle
private const val LinearAnimationDuration = 1800

// Duration of the head and tail animations for both lines
private const val FirstLineHeadDuration = 750
private const val FirstLineTailDuration = 850
private const val SecondLineHeadDuration = 567
private const val SecondLineTailDuration = 533

// Delay before the start of the head and tail animations for both lines
private const val FirstLineHeadDelay = 0
private const val FirstLineTailDelay = 333
private const val SecondLineHeadDelay = 1000
private const val SecondLineTailDelay = 1267

private val FirstLineHeadEasing = CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
private val FirstLineTailEasing = CubicBezierEasing(0.4f, 0f, 1f, 1f)
private val SecondLineHeadEasing = CubicBezierEasing(0f, 0f, 0.65f, 1f)
private val SecondLineTailEasing = CubicBezierEasing(0.1f, 0f, 0.45f, 1f)

@Preview
@Composable
private fun LinearProgressIndicatorPreview() {
    SudokuTheme {
        LinearProgressIndicator()
    }
}
