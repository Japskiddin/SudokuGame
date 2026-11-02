package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import io.github.japskiddin.sudoku.core.designsystem.resources.Res
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_black
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_black_italic
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_bold
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_bold_italic
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_italic
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_light
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_light_italic
import io.github.japskiddin.sudoku.core.designsystem.resources.merriweather_regular
import org.jetbrains.compose.resources.Font

public val MerriweatherFamily: FontFamily
    @Composable get() = FontFamily(
        Font(Res.font.merriweather_light, FontWeight.Light),
        Font(Res.font.merriweather_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.merriweather_regular, FontWeight.Normal),
        Font(Res.font.merriweather_italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.merriweather_bold, FontWeight.Bold),
        Font(Res.font.merriweather_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.merriweather_black, FontWeight.Black),
        Font(Res.font.merriweather_black_italic, FontWeight.Black, FontStyle.Italic),
    )

@Composable
internal fun rememberTypography(): Typography {
    val fontFamily = MerriweatherFamily
    return remember(fontFamily) { Typography(fontFamily = fontFamily) }
}
