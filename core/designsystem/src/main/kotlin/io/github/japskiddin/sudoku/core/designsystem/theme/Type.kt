package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Title Large - for Application's title
 * Title Medium - for App bar's title
 * Title Small - for year on title screen
 * Body Large - for titles on info panels (Loading, Errors, etc)
 * Body Medium - for dialogs
 * Body Small - for game info
 * Label Medium - for game buttons
 * Label Small - for tool buttons
 */
@Immutable
public data class Typography(
    val titleLarge: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 48.sp
    ),
    val titleMedium: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp
    ),
    val titleSmall: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp
    ),
    val bodyLarge: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 36.sp
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val bodySmall: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val labelMedium: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    val labelSmall: TextStyle = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { Typography() }
