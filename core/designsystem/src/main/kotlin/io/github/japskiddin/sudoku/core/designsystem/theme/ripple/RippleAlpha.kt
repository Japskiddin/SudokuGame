package io.github.japskiddin.sudoku.core.designsystem.theme.ripple

import androidx.compose.runtime.Immutable

@Immutable
public class RippleAlpha(
    public val draggedAlpha: Float,
    public val focusedAlpha: Float,
    public val hoveredAlpha: Float,
    public val pressedAlpha: Float
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RippleAlpha) return false

        if (draggedAlpha != other.draggedAlpha) return false
        if (focusedAlpha != other.focusedAlpha) return false
        if (hoveredAlpha != other.hoveredAlpha) return false
        if (pressedAlpha != other.pressedAlpha) return false

        return true
    }

    override fun hashCode(): Int {
        var result = draggedAlpha.hashCode()
        result = 31 * result + focusedAlpha.hashCode()
        result = 31 * result + hoveredAlpha.hashCode()
        result = 31 * result + pressedAlpha.hashCode()
        return result
    }

    override fun toString(): String {
        return "RippleAlpha(draggedAlpha=$draggedAlpha, focusedAlpha=$focusedAlpha, " +
            "hoveredAlpha=$hoveredAlpha, pressedAlpha=$pressedAlpha)"
    }
}
