package io.github.japskiddin.sudoku.core.designsystem.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
private val AtomicTangerine: Color = Color(0xFFFAA468)

@Stable
private val Rope: Color = Color(0xFF8A4E24)

@Stable
private val DenimBlue: Color = Color(0xFF68BEFA)

@Stable
private val SilkBlue: Color = Color(0xFF428EC2)

@Stable
private val LightNavyBlue: Color = Color(0xFF235F89)

@Stable
private val Elephant: Color = Color(0xFF10354F)

@Stable
private val HippieBlue: Color = Color(0xFF3E9CB1)

@Stable
private val GreenishBlue: Color = Color(0xFF277E91)

@Suppress("LongParameterList")
public class Colors(
    primary: Color,
    onPrimary: Color,
    background: Color,
    onBackground: Color,
    dialog: Color,
    onDialog: Color,
    menuButtonForegroundNormal: Color,
    menuButtonForegroundPressed: Color,
    menuButtonBackgroundNormal: Color,
    menuButtonBackgroundPressed: Color,
    onMenuButton: Color,
    boardCellNormal: Color,
    boardCellSelected: Color,
    boardNumberNormal: Color,
    boardNumberSelected: Color,
    switchUncheckedThumb: Color,
    switchUncheckedTrack: Color,
    switchCheckedThumb: Color,
    switchCheckedTrack: Color,
    switchBorder: Color,
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
    public var menuButtonForegroundNormal: Color by mutableStateOf(menuButtonForegroundNormal)
        private set
    public var menuButtonForegroundPressed: Color by mutableStateOf(menuButtonForegroundPressed)
        private set
    public var menuButtonBackgroundNormal: Color by mutableStateOf(menuButtonBackgroundNormal)
        private set
    public var menuButtonBackgroundPressed: Color by mutableStateOf(menuButtonBackgroundPressed)
        private set
    public var onMenuButton: Color by mutableStateOf(onMenuButton)
        private set
    public var boardCellNormal: Color by mutableStateOf(boardCellNormal)
        private set
    public var boardCellSelected: Color by mutableStateOf(boardCellSelected)
        private set
    public var boardNumberNormal: Color by mutableStateOf(boardNumberNormal)
        private set
    public var boardNumberSelected: Color by mutableStateOf(boardNumberSelected)
        private set
    public var switchUncheckedThumb: Color by mutableStateOf(switchUncheckedThumb)
        private set
    public var switchUncheckedTrack: Color by mutableStateOf(switchUncheckedTrack)
        private set
    public var switchCheckedThumb: Color by mutableStateOf(switchCheckedThumb)
        private set
    public var switchCheckedTrack: Color by mutableStateOf(switchCheckedTrack)
        private set
    public var switchBorder: Color by mutableStateOf(switchBorder)
        private set

    public fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        dialog: Color = this.dialog,
        onDialog: Color = this.onDialog,
        menuButtonForegroundNormal: Color = this.menuButtonForegroundNormal,
        menuButtonForegroundPressed: Color = this.menuButtonForegroundPressed,
        menuButtonBackgroundNormal: Color = this.menuButtonBackgroundNormal,
        menuButtonBackgroundPressed: Color = this.menuButtonBackgroundPressed,
        onMenuButton: Color = this.onMenuButton,
        boardCellNormal: Color = this.boardCellNormal,
        boardCellSelected: Color = this.boardCellSelected,
        boardNumberNormal: Color = this.boardNumberNormal,
        boardNumberSelected: Color = this.boardNumberSelected,
        switchUncheckedThumb: Color = this.switchUncheckedThumb,
        switchUncheckedTrack: Color = this.switchUncheckedTrack,
        switchCheckedThumb: Color = this.switchCheckedThumb,
        switchCheckedTrack: Color = this.switchCheckedTrack,
        switchBorder: Color = this.switchBorder,
    ): Colors = Colors(
        primary = primary,
        onPrimary = onPrimary,
        background = background,
        onBackground = onBackground,
        dialog = dialog,
        onDialog = onDialog,
        menuButtonForegroundNormal = menuButtonForegroundNormal,
        menuButtonForegroundPressed = menuButtonForegroundPressed,
        menuButtonBackgroundNormal = menuButtonBackgroundNormal,
        menuButtonBackgroundPressed = menuButtonBackgroundPressed,
        onMenuButton = onMenuButton,
        boardCellNormal = boardCellNormal,
        boardCellSelected = boardCellSelected,
        boardNumberNormal = boardNumberNormal,
        boardNumberSelected = boardNumberSelected,
        switchUncheckedThumb = switchUncheckedThumb,
        switchUncheckedTrack = switchUncheckedTrack,
        switchCheckedThumb = switchCheckedThumb,
        switchCheckedTrack = switchCheckedTrack,
        switchBorder = switchBorder,
    )

    public fun updateColorsFrom(other: Colors) {
        primary = other.primary
        onPrimary = other.onPrimary
        background = other.background
        onBackground = other.onBackground
        dialog = other.dialog
        onDialog = other.onDialog
        menuButtonForegroundNormal = other.menuButtonForegroundNormal
        menuButtonForegroundPressed = other.menuButtonForegroundPressed
        menuButtonBackgroundNormal = other.menuButtonBackgroundNormal
        menuButtonBackgroundPressed = other.menuButtonBackgroundPressed
        onMenuButton = other.onMenuButton
        boardCellNormal = other.boardCellNormal
        boardCellSelected = other.boardCellSelected
        boardNumberNormal = other.boardNumberNormal
        boardNumberSelected = other.boardNumberSelected
        switchUncheckedThumb = other.switchUncheckedThumb
        switchUncheckedTrack = other.switchUncheckedTrack
        switchCheckedThumb = other.switchCheckedThumb
        switchCheckedTrack = other.switchCheckedTrack
        switchBorder = other.switchBorder
    }
}

internal val ThemeColors: Colors = Colors(
    primary = AtomicTangerine,
    onPrimary = Color.White,
    background = AtomicTangerine,
    onBackground = Color.White,
    dialog = Color.White,
    onDialog = Color.Black,
    menuButtonForegroundNormal = HippieBlue,
    menuButtonForegroundPressed = GreenishBlue,
    menuButtonBackgroundNormal = LightNavyBlue,
    menuButtonBackgroundPressed = Elephant,
    onMenuButton = Color.White,
    boardCellNormal = Color.White,
    boardCellSelected = DenimBlue,
    boardNumberNormal = Color.Black,
    boardNumberSelected = Color.White,
    switchUncheckedThumb = Rope,
    switchUncheckedTrack = Color.White,
    switchCheckedThumb = Color.White,
    switchCheckedTrack = Rope,
    switchBorder = Color.Black,
)

internal val LocalColors = staticCompositionLocalOf { ThemeColors }
