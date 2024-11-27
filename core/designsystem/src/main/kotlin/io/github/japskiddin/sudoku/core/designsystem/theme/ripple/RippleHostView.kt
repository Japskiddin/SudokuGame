package io.github.japskiddin.sudoku.core.designsystem.theme.ripple

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.lang.reflect.Method
import kotlin.math.roundToInt

internal class RippleHostView(context: Context) : View(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0, 0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // noop
    }

    override fun refreshDrawableState() {
        // We don't want the View to manage the drawable state, so avoid updating the ripple's
        // state (via View.mBackground) when we lose window focus, or other events.
    }

    private var ripple: UnprojectedRipple? = null
    private var bounded: Boolean? = null

    private var lastRippleStateChangeTimeMillis: Long? = null

    private var resetRippleRunnable: Runnable? = null

    private fun createRipple(bounded: Boolean) {
        ripple =
            UnprojectedRipple(bounded).apply {
                // Set the ripple to be the view's background - this will internally set the
                // ripple's
                // Drawable.Callback callback to equal this view so there is no need to manage this
                // separately.
                background = this
            }
    }

    private var onInvalidateRipple: (() -> Unit)? = null

    override fun invalidateDrawable(who: Drawable) {
        onInvalidateRipple?.invoke()
    }

    fun addRipple(
        interaction: PressInteraction.Press,
        bounded: Boolean,
        size: Size,
        radius: Int,
        color: Color,
        alpha: Float,
        onInvalidateRipple: () -> Unit
    ) {
        // Create a new ripple if there is no existing ripple, or bounded has changed.
        // (Since this.bounded is initialized to `null`, technically the first check isn't
        // needed, but it might not survive refactoring).
        if (ripple == null || bounded != this.bounded) {
            createRipple(bounded)
            this.bounded = bounded
        }
        val ripple = ripple!!
        this.onInvalidateRipple = onInvalidateRipple
        setRippleProperties(size, radius, color, alpha)
        if (bounded) {
            // Bounded ripples should animate from the press position
            ripple.setHotspot(interaction.pressPosition.x, interaction.pressPosition.y)
        } else {
            // Unbounded ripples should animate from the center of the ripple - in the framework
            // this change in spec was never made, so they currently animate from the press
            // position into a circle that starts at the center of the ripple, instead of
            // starting directly at the center.
            ripple.setHotspot(ripple.bounds.centerX().toFloat(), ripple.bounds.centerY().toFloat())
        }
        setRippleState(pressed = true)
    }

    fun removeRipple() {
        setRippleState(pressed = false)
    }

    fun setRippleProperties(size: Size, radius: Int, color: Color, alpha: Float) {
        val ripple = ripple ?: return
        // NOTE: if adding new properties here, make sure they are guarded with an equality check
        // (either here or internally in RippleDrawable). Many properties invalidate the ripple when
        // changed, which will lead to a call to updateRippleProperties again, which will cause
        // another invalidation, etc.
        // Note: for cases where size and radius are updated during an existing ripple, the radius
        // must be set first - changing the bounds is what causes the ripple to be updated,
        // changing the radius on its own will not update the ripple.
        ripple.trySetRadius(radius)
        ripple.setColor(color, alpha)
        val newBounds = Rect(0, 0, size.width.roundToInt(), size.height.roundToInt())
        // Drawing the background causes the view to update the bounds of the drawable
        // based on the view's bounds, so we need to adjust the view itself to match the
        // canvas' bounds.
        // These setters will no-op if there is no change, so no need for us to check for equality
        left = newBounds.left
        top = newBounds.top
        right = newBounds.right
        bottom = newBounds.bottom
        ripple.bounds = newBounds
    }

    fun disposeRipple() {
        onInvalidateRipple = null
        if (resetRippleRunnable != null) {
            removeCallbacks(resetRippleRunnable)
            resetRippleRunnable!!.run()
        } else {
            ripple?.state = RestingState
        }
        val ripple = ripple ?: return
        ripple.setVisible(false, false)
        unscheduleDrawable(ripple)
    }

    private fun setRippleState(pressed: Boolean) {
        val currentTime = AnimationUtils.currentAnimationTimeMillis()
        resetRippleRunnable?.let { runnable ->
            removeCallbacks(runnable)
            runnable.run()
        }
        val timeSinceLastStateChange = currentTime - (lastRippleStateChangeTimeMillis ?: 0)
        // When fading out, if the exit happens on the same millisecond (as returned by
        // currentAnimationTimeMillis), RippleForeground will instantly fade out without showing
        // the minimum duration ripple. Handle this specific case by posting the exit event to
        // make sure it is shown for its minimum time, if the last state change was recent.
        // Since it is possible for currentAnimationTimeMillis to be different between here, and
        // when it is called inside RippleForeground, we post for any small difference just to be
        // safe.
        if (!pressed && timeSinceLastStateChange < MinimumRippleStateChangeTime) {
            resetRippleRunnable = Runnable {
                ripple?.state = RestingState
                resetRippleRunnable = null
            }
            postDelayed(resetRippleRunnable, ResetRippleDelayDuration)
        } else {
            val state = if (pressed) PressedState else RestingState
            ripple?.state = state
        }
        lastRippleStateChangeTimeMillis = currentTime
    }

    companion object {
        private const val MinimumRippleStateChangeTime = 5L

        private const val ResetRippleDelayDuration = 50L

        private val PressedState =
            intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        private val RestingState = intArrayOf()
    }
}

private class UnprojectedRipple(private val bounded: Boolean) :
    RippleDrawable(
        // Temporary default color that we will override later
        /* color */
        ColorStateList.valueOf(android.graphics.Color.BLACK),
        /* content */
        null,
        // The color of the mask here doesn't matter - we just need a mask to draw the bounded
        // ripple
        // against
        /* mask */
        if (bounded) ColorDrawable(android.graphics.Color.WHITE) else null
    ) {
    private var rippleColor: Color? = null

    private var rippleRadius: Int? = null

    fun setColor(color: Color, alpha: Float) {
        val newColor = calculateRippleColor(color, alpha)
        if (rippleColor != newColor) {
            rippleColor = newColor
            setColor(ColorStateList.valueOf(newColor.toArgb()))
        }
    }

    private var projected = false

    override fun isProjected(): Boolean {
        return projected
    }

    override fun getDirtyBounds(): Rect {
        if (!bounded) {
            projected = true
        }
        val bounds = super.getDirtyBounds()
        projected = false
        return bounds
    }

    fun trySetRadius(radius: Int) {
        if (rippleRadius != radius) {
            rippleRadius = radius
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                try {
                    if (!setMaxRadiusFetched) {
                        setMaxRadiusFetched = true
                        setMaxRadiusMethod =
                            RippleDrawable::class
                                .java
                                .getDeclaredMethod("setMaxRadius", Int::class.javaPrimitiveType)
                    }
                    setMaxRadiusMethod?.invoke(this, radius)
                } catch (e: Exception) {
                    // Fail silently
                }
            } else {
                MRadiusHelper.setRadius(this, radius)
            }
        }
    }

    private fun calculateRippleColor(color: Color, alpha: Float): Color {
        // On API 21-27 the ripple animation is split into two sections - an overlay and an
        // animation on top - and 50% of the original alpha is used for both. Since these sections
        // don't always overlap, the actual alpha of the animation in parts can be 50% of the
        // original amount, so to ensure that the contrast is correct, and make the ripple alpha
        // match more closely with the provided value, we double it first.
        // Note that this is also consistent with MDC behavior.
        val transformedAlpha =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                alpha * 2
            } else {
                // Note: above 28 the ripple alpha is clamped to 50%, so this might not be the
                // _actual_ alpha that is used in the ripple.
                alpha
            }
                .coerceAtMost(1f)
        return color.copy(alpha = transformedAlpha)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private object MRadiusHelper {
        /** Sets the [radius] for the given [ripple]. */
        fun setRadius(ripple: RippleDrawable, radius: Int) {
            ripple.radius = radius
        }
    }

    companion object {
        private var setMaxRadiusMethod: Method? = null
        private var setMaxRadiusFetched = false
    }
}
