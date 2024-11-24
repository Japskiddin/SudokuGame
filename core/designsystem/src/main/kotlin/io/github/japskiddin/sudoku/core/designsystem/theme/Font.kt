package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import io.github.japskiddin.sudoku.core.designsystem.R

@Stable
public val MerriweatherFamily: FontFamily = FontFamily(
    Font(R.font.merriweather_light, FontWeight.Light),
    Font(R.font.merriweather_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.merriweather_regular, FontWeight.Normal),
    Font(R.font.merriweather_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.merriweather_bold, FontWeight.Bold),
    Font(R.font.merriweather_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.merriweather_black, FontWeight.Black),
    Font(R.font.merriweather_black_italic, FontWeight.Black, FontStyle.Italic)
)
