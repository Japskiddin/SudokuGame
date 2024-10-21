package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Title Large - for Application's title
 * Title Small - for year on title screen
 * Body Large - for titles on info panels (Loading, Errors, etc)
 * Body Medium - for dialogs
 * Label Medium - for game buttons
 * Label Small - for tool buttons
 */
public val Typography: Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 48.sp
    ),
    titleSmall = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 36.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MerriweatherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    )
)
