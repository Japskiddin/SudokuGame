package io.github.japskiddin.sudoku.feature.game.ui.component.autosizetext

import androidx.compose.runtime.Immutable
import kotlin.reflect.KProperty

@Immutable
public data class ImmutableWrapper<T>(val value: T)

public fun <T> T.toImmutableWrapper(): ImmutableWrapper<T> = ImmutableWrapper(this)

public operator fun <T> ImmutableWrapper<T>.getValue(thisRef: Any?, property: KProperty<*>): T =
    value