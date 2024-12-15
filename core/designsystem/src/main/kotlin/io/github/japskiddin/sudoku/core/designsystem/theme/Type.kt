@file:Suppress("MatchingDeclarationName")

package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
public data class Typography(
    val appTitle: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 48.sp
    ),
    val appBar: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp
    ),
    val info: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp
    ),
    val panel: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 36.sp
    ),
    val dialog: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val gameInfo: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val gameButton: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val toolButton: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { Typography() }
