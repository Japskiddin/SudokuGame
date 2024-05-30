package io.github.japskiddin.sudoku.feature.game.ui.component.autosizetext

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified

// DP
public fun Density.toSp(dp: Dp): TextUnit = dp.toSp()
public fun Density.toPx(dp: Dp): Float = dp.toPx()
public fun Density.roundToPx(dp: Dp): Int = dp.roundToPx()

// TEXT UNIT
public fun Density.toDp(sp: TextUnit): Dp = sp.toDp()
public fun Density.toPx(sp: TextUnit): Float = sp.toPx()
public fun Density.roundToPx(sp: TextUnit): Int = sp.roundToPx()

// FLOAT
public fun Density.toDp(px: Float): Dp = px.toDp()
public fun Density.toSp(px: Float): TextUnit = px.toSp()

// INT
public fun Density.toDp(px: Int): Dp = px.toDp()
public fun Density.toSp(px: Int): TextUnit = px.toSp()

// SIZE
public fun Density.toIntSize(dpSize: DpSize): IntSize =
  IntSize(dpSize.width.roundToPx(), dpSize.height.roundToPx())

public fun Density.toSize(dpSize: DpSize): Size =
  if (dpSize.isSpecified) Size(dpSize.width.toPx(), dpSize.height.toPx())
  else Size.Unspecified

public fun Density.toDpSize(size: Size): DpSize =
  if (size.isSpecified) DpSize(size.width.toDp(), size.height.toDp())
  else DpSize.Unspecified

public fun Density.toDpSize(intSize: IntSize): DpSize =
  DpSize(intSize.width.toDp(), intSize.height.toDp())

// OFFSET
public fun Density.toIntOffset(dpOffset: DpOffset): IntOffset =
  IntOffset(dpOffset.x.roundToPx(), dpOffset.y.roundToPx())

public fun Density.toOffset(dpOffset: DpOffset): Offset =
  if (dpOffset.isSpecified) Offset(dpOffset.x.toPx(), dpOffset.y.toPx())
  else Offset.Unspecified

public fun Density.toDpOffset(offset: Offset): DpOffset =
  if (offset.isSpecified) DpOffset(offset.x.toDp(), offset.y.toDp())
  else DpOffset.Unspecified

public fun Density.toDpOffset(intOffset: IntOffset): DpOffset =
  DpOffset(intOffset.x.toDp(), intOffset.y.toDp())