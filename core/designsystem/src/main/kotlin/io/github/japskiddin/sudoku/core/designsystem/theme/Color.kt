package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
private val AtomicTangerine: Color = Color(0xFFFAA468)

public val Primary: Color = Color(0xFFFAA468)
public val OnPrimary: Color = Color(0xFFFFFFFF)

public val PrimaryDark: Color = Color(0xFF8A4E24)
public val OnPrimaryDark: Color = Color(0xFFFFFFFF)

public val MenuButtonForegroundNormal: Color = Color(0xFF68BEFA)
public val MenuButtonForegroundPressed: Color = Color(0xFF428EC2)

public val MenuButtonBackgroundNormal: Color = Color(0xFF235F89)
public val MenuButtonBackgroundPressed: Color = Color(0xFF10354F)

public val OnMenuButton: Color = Color(0xFFFFFFFF)

public val DialogSurface: Color = Color(0xFFFFFFFF)
public val OnDialogSurface: Color = Color(0xFF000000)

public val BoardCellNormal: Color = Color(0xFFFFFFFF)
public val BoardCellSelected: Color = Color(0xFF68BEFA)

public val BoardNumberNormal: Color = Color(0xFF000000)
public val BoardNumberSelected: Color = Color(0xFFFFFFFF)

public val SettingsSwitchUncheckedThumb: Color = Color(0xFF68BEFA)
public val SettingsSwitchUncheckedTrack: Color = Color(0xFFFFFFFF)
public val SettingsSwitchUncheckedBorder: Color = Color(0xFF10354F)
public val SettingsSwitchCheckedThumb: Color = Color(0xFFFFFFFF)
public val SettingsSwitchCheckedTrack: Color = Color(0xFF68BEFA)
public val SettingsSwitchCheckedBorder: Color = Color(0xFF10354F)

public class Colors(
    primary: Color,
    onPrimary: Color,
    background: Color,
    onBackground: Color,
    dialog: Color,
    onDialog: Color,
) {
    public var primary: Color by mutableStateOf(primary)
        private set
    public var onPrimary: Color by mutableStateOf(onPrimary)
        private set
    public var background: Color by mutableStateOf(background)
        private set
    public var onBackground: Color by mutableStateOf(onBackground)
        private set
    public var dialog: Color by mutableStateOf(dialog)
        private set
    public var onDialog: Color by mutableStateOf(onDialog)
        private set

    public fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        dialog: Color = this.dialog,
        onDialog: Color = this.onDialog,
    ): Colors = Colors(
        primary = primary,
        onPrimary = onPrimary,
        background = background,
        onBackground = onBackground,
        dialog = dialog,
        onDialog = onDialog,
    )

    public fun updateColorsFrom(other: Colors) {
        primary = other.primary
        onPrimary = other.onPrimary
        background = other.background
        onBackground = other.onBackground
        dialog = other.dialog
        onDialog = other.onDialog
    }
}

public val ThemeColors: Colors = Colors(
    primary = AtomicTangerine,
    onPrimary = Color.White,
    background = AtomicTangerine,
    onBackground = Color.White,
    dialog = Color.White,
    onDialog = Color.Black,
)

internal val LocalColors = staticCompositionLocalOf { ThemeColors }
