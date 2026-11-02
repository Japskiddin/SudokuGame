package io.github.japskiddin.sudoku.core.designsystem.theme.ripple

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.unit.Dp

internal actual fun createPlatformRippleNode(
    interactionSource: InteractionSource,
    bounded: Boolean,
    radius: Dp,
    color: ColorProducer,
    rippleAlpha: () -> RippleAlpha
): DelegatableNode = CommonRippleNode(interactionSource, bounded, radius, color, rippleAlpha)
