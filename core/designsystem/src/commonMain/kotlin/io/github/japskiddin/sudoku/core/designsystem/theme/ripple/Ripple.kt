package io.github.japskiddin.sudoku.core.designsystem.theme.ripple

import androidx.collection.mutableObjectListOf
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
public fun ripple(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified
): IndicationNodeFactory =
    if (radius == Dp.Unspecified && color == Color.Unspecified) {
        if (bounded) DefaultBoundedRipple else DefaultUnboundedRipple
    } else {
        RippleNodeFactory(bounded, radius, color)
    }

@Stable
public fun ripple(
    color: ColorProducer,
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified
): IndicationNodeFactory = RippleNodeFactory(bounded, radius, color)

private fun createRippleModifierNode(
    interactionSource: InteractionSource,
    bounded: Boolean,
    radius: Dp,
    color: ColorProducer,
    rippleAlpha: () -> RippleAlpha
): DelegatableNode = createPlatformRippleNode(interactionSource, bounded, radius, color, rippleAlpha)

private object RippleDefaults {
    val RippleAlpha: RippleAlpha =
        RippleAlpha(
            pressedAlpha = StateTokens.PRESSED_STATE_LAYER_OPACITY,
            focusedAlpha = StateTokens.FOCUS_STATE_LAYER_OPACTITY,
            draggedAlpha = StateTokens.DRAGGED_STATE_LAYER_OPACITY,
            hoveredAlpha = StateTokens.HOVER_STATE_LAYER_OPACITY
        )
}

public val LocalRippleConfiguration: ProvidableCompositionLocal<RippleConfiguration?> =
    compositionLocalOf { RippleConfiguration() }

@Immutable
public class RippleConfiguration(
    public val color: Color = Color.Unspecified,
    public val rippleAlpha: RippleAlpha? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RippleConfiguration) return false

        if (color != other.color) return false
        if (rippleAlpha != other.rippleAlpha) return false

        return true
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + (rippleAlpha?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "RippleConfiguration(color=$color, rippleAlpha=$rippleAlpha)"
    }
}

@Stable
private class RippleNodeFactory
private constructor(
    private val bounded: Boolean,
    private val radius: Dp,
    private val colorProducer: ColorProducer?,
    private val color: Color
) : IndicationNodeFactory {
    constructor(
        bounded: Boolean,
        radius: Dp,
        colorProducer: ColorProducer
    ) : this(bounded, radius, colorProducer, Color.Unspecified)

    constructor(bounded: Boolean, radius: Dp, color: Color) : this(bounded, radius, null, color)

    override fun create(interactionSource: InteractionSource): DelegatableNode {
        val colorProducer = colorProducer ?: ColorProducer { color }
        return DelegatingThemeAwareRippleNode(interactionSource, bounded, radius, colorProducer)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RippleNodeFactory) return false

        if (bounded != other.bounded) return false
        if (radius != other.radius) return false
        if (colorProducer != other.colorProducer) return false
        return color == other.color
    }

    override fun hashCode(): Int {
        var result = bounded.hashCode()
        result = 31 * result + radius.hashCode()
        result = 31 * result + colorProducer.hashCode()
        result = 31 * result + color.hashCode()
        return result
    }
}

private class DelegatingThemeAwareRippleNode(
    private val interactionSource: InteractionSource,
    private val bounded: Boolean,
    private val radius: Dp,
    private val color: ColorProducer,
) : DelegatingNode(), CompositionLocalConsumerModifierNode, ObserverModifierNode {
    private var rippleNode: DelegatableNode? = null

    override fun onAttach() {
        updateConfiguration()
    }

    override fun onObservedReadsChanged() {
        updateConfiguration()
    }

    private fun updateConfiguration() {
        observeReads {
            val configuration = currentValueOf(LocalRippleConfiguration)
            if (configuration == null) {
                removeRipple()
            } else {
                if (rippleNode == null) attachNewRipple()
            }
        }
    }

    private fun attachNewRipple() {
        val calculateColor = ColorProducer {
            val userDefinedColor = color()
            if (userDefinedColor.isSpecified) {
                userDefinedColor
            } else {
                // If this is null, the ripple will be removed, so this should always be non-null in
                // normal use
                val rippleConfiguration = currentValueOf(LocalRippleConfiguration)
                if (rippleConfiguration?.color?.isSpecified == true) {
                    rippleConfiguration.color
                } else {
                    currentValueOf(LocalContentColor)
                }
            }
        }

        val calculateRippleAlpha = {
            // If this is null, the ripple will be removed, so this should always be non-null in
            // normal use
            val rippleConfiguration = currentValueOf(LocalRippleConfiguration)
            rippleConfiguration?.rippleAlpha ?: RippleDefaults.RippleAlpha
        }

        rippleNode =
            delegate(
                createRippleModifierNode(
                    interactionSource,
                    bounded,
                    radius,
                    calculateColor,
                    calculateRippleAlpha
                )
            )
    }

    private fun removeRipple() {
        rippleNode?.let { undelegate(it) }
        rippleNode = null
    }
}

private val DefaultBoundedRipple =
    RippleNodeFactory(bounded = true, radius = Dp.Unspecified, color = Color.Unspecified)
private val DefaultUnboundedRipple =
    RippleNodeFactory(bounded = false, radius = Dp.Unspecified, color = Color.Unspecified)

internal expect fun createPlatformRippleNode(
    interactionSource: InteractionSource,
    bounded: Boolean,
    radius: Dp,
    color: ColorProducer,
    rippleAlpha: () -> RippleAlpha
): DelegatableNode

internal abstract class RippleNode(
    private val interactionSource: InteractionSource,
    protected val bounded: Boolean,
    private val radius: Dp,
    private val color: ColorProducer,
    protected val rippleAlpha: () -> RippleAlpha
) :
    Modifier.Node(),
    CompositionLocalConsumerModifierNode,
    DrawModifierNode,
    LayoutAwareModifierNode {
    final override val shouldAutoInvalidate: Boolean = false

    private var stateLayer: StateLayer? = null

    // The following are calculated inside onRemeasured(). These must be initialized before adding
    // a ripple.

    protected var targetRadius: Float = 0f

    // The size is needed for Android to update ripple bounds if the size changes
    protected var rippleSize: Size = Size.Zero
        private set

    val rippleColor: Color
        get() = color()

    // Track interactions that were emitted before we have been placed - we need to wait until we
    // have a valid size in order to set the radius and size correctly.
    private var hasValidSize = false
    private val pendingInteractions = mutableObjectListOf<PressInteraction>()

    override fun onRemeasured(size: IntSize) {
        hasValidSize = true
        val density = requireDensity()
        rippleSize = size.toSize()
        targetRadius =
            with(density) {
                if (radius.isUnspecified) {
                    // Explicitly calculate the radius instead of using RippleDrawable.RADIUS_AUTO
                    // on
                    // Android since the latest spec does not match with the existing radius
                    // calculation
                    // in the framework.
                    getRippleEndRadius(bounded, rippleSize)
                } else {
                    radius.toPx()
                }
            }
        // Flush any pending interactions that were waiting for measurement
        pendingInteractions.forEach { handlePressInteraction(it) }
        pendingInteractions.clear()
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction -> {
                        if (hasValidSize) {
                            handlePressInteraction(interaction)
                        } else {
                            // Handle these later when we have a valid size
                            pendingInteractions += interaction
                        }
                    }

                    else -> updateStateLayer(interaction, this)
                }
            }
        }
    }

    private fun handlePressInteraction(pressInteraction: PressInteraction) {
        when (pressInteraction) {
            is PressInteraction.Press -> addRipple(pressInteraction, rippleSize, targetRadius)
            is PressInteraction.Release -> removeRipple(pressInteraction.press)
            is PressInteraction.Cancel -> removeRipple(pressInteraction.press)
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        stateLayer?.run { drawStateLayer(targetRadius, rippleColor) }
        drawRipples()
    }

    abstract fun DrawScope.drawRipples()

    abstract fun addRipple(interaction: PressInteraction.Press, size: Size, targetRadius: Float)

    abstract fun removeRipple(interaction: PressInteraction.Press)

    private fun updateStateLayer(interaction: Interaction, scope: CoroutineScope) {
        val stateLayer =
            stateLayer
                ?: StateLayer(bounded, rippleAlpha).also { instance ->
                    // Invalidate when adding the state layer so we can start drawing it
                    invalidateDraw()
                    stateLayer = instance
                }
        stateLayer.handleInteraction(interaction, scope)
    }
}

private class StateLayer(private val bounded: Boolean, private val rippleAlpha: () -> RippleAlpha) {
    private val animatedAlpha = Animatable(0f)

    private val interactions: MutableList<Interaction> = mutableListOf()
    private var currentInteraction: Interaction? = null

    @Suppress("CyclomaticComplexMethod")
    fun handleInteraction(interaction: Interaction, scope: CoroutineScope) {
        when (interaction) {
            is HoverInteraction.Enter -> {
                interactions.add(interaction)
            }

            is HoverInteraction.Exit -> {
                interactions.remove(interaction.enter)
            }

            is FocusInteraction.Focus -> {
                interactions.add(interaction)
            }

            is FocusInteraction.Unfocus -> {
                interactions.remove(interaction.focus)
            }

            is DragInteraction.Start -> {
                interactions.add(interaction)
            }

            is DragInteraction.Stop -> {
                interactions.remove(interaction.start)
            }

            is DragInteraction.Cancel -> {
                interactions.remove(interaction.start)
            }

            else -> return
        }

        // The most recent interaction is the one we want to show
        val newInteraction = interactions.lastOrNull()

        if (currentInteraction != newInteraction) {
            if (newInteraction != null) {
                val rippleAlpha = rippleAlpha()
                val targetAlpha =
                    when (interaction) {
                        is HoverInteraction.Enter -> rippleAlpha.hoveredAlpha
                        is FocusInteraction.Focus -> rippleAlpha.focusedAlpha
                        is DragInteraction.Start -> rippleAlpha.draggedAlpha
                        else -> 0f
                    }
                val incomingAnimationSpec = incomingStateLayerAnimationSpecFor(newInteraction)

                scope.launch { animatedAlpha.animateTo(targetAlpha, incomingAnimationSpec) }
            } else {
                val outgoingAnimationSpec = outgoingStateLayerAnimationSpecFor(currentInteraction)

                scope.launch { animatedAlpha.animateTo(0f, outgoingAnimationSpec) }
            }
            currentInteraction = newInteraction
        }
    }

    fun DrawScope.drawStateLayer(radius: Float, color: Color) {
        val alpha = animatedAlpha.value

        if (alpha > 0f) {
            val modulatedColor = color.copy(alpha = alpha)

            if (bounded) {
                clipRect { drawCircle(modulatedColor, radius) }
            } else {
                drawCircle(modulatedColor, radius)
            }
        }
    }
}

private fun incomingStateLayerAnimationSpecFor(interaction: Interaction): AnimationSpec<Float> {
    return when (interaction) {
        is HoverInteraction.Enter -> DefaultTweenSpec
        is FocusInteraction.Focus -> TweenSpec(durationMillis = 45, easing = LinearEasing)
        is DragInteraction.Start -> TweenSpec(durationMillis = 45, easing = LinearEasing)
        else -> DefaultTweenSpec
    }
}

private fun outgoingStateLayerAnimationSpecFor(interaction: Interaction?): AnimationSpec<Float> {
    return when (interaction) {
        is HoverInteraction.Enter -> DefaultTweenSpec
        is FocusInteraction.Focus -> DefaultTweenSpec
        is DragInteraction.Start -> TweenSpec(durationMillis = 150, easing = LinearEasing)
        else -> DefaultTweenSpec
    }
}

private val DefaultTweenSpec = TweenSpec<Float>(durationMillis = 15, easing = LinearEasing)

private object StateTokens {
    const val DRAGGED_STATE_LAYER_OPACITY = 0.16f
    const val FOCUS_STATE_LAYER_OPACTITY = 0.1f
    const val HOVER_STATE_LAYER_OPACITY = 0.08f
    const val PRESSED_STATE_LAYER_OPACITY = 0.1f
}

private val LocalContentColor = compositionLocalOf { Color.Black }
