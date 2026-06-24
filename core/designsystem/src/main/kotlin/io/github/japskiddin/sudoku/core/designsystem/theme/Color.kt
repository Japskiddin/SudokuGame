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
    gameButtonForegroundNormal: Color,
    gameButtonForegroundPressed: Color,
    gameButtonBackgroundNormal: Color,
    gameButtonBackgroundPressed: Color,
    onGameButton: Color,
    boardCellNormal: Color,
    boardCellSelected: Color,
    boardNumberNormal: Color,
    boardNumberSelected: Color,
    boardNumberError: Color,
    boardNumberLocked: Color,
    switchUncheckedThumb: Color,
    switchUncheckedTrack: Color,
    switchCheckedThumb: Color,
    switchCheckedTrack: Color,
    switchBorder: Color,
    gamePanelNormal: Color,
    gamePanelPressed: Color,
    card: Color,
    onCard: Color,
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
    public var gameButtonForegroundNormal: Color by mutableStateOf(gameButtonForegroundNormal)
        private set
    public var gameButtonForegroundPressed: Color by mutableStateOf(gameButtonForegroundPressed)
        private set
    public var gameButtonBackgroundNormal: Color by mutableStateOf(gameButtonBackgroundNormal)
        private set
    public var gameButtonBackgroundPressed: Color by mutableStateOf(gameButtonBackgroundPressed)
        private set
    public var onGameButton: Color by mutableStateOf(onGameButton)
        private set
    public var boardCellNormal: Color by mutableStateOf(boardCellNormal)
        private set
    public var boardCellSelected: Color by mutableStateOf(boardCellSelected)
        private set
    public var boardNumberNormal: Color by mutableStateOf(boardNumberNormal)
        private set
    public var boardNumberSelected: Color by mutableStateOf(boardNumberSelected)
        private set
    public var boardNumberError: Color by mutableStateOf(boardNumberError)
        private set
    public var boardNumberLocked: Color by mutableStateOf(boardNumberLocked)
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
    public var gamePanelNormal: Color by mutableStateOf(gamePanelNormal)
        private set
    public var gamePanelPressed: Color by mutableStateOf(gamePanelPressed)
        private set
    public var card: Color by mutableStateOf(card)
        private set
    public var onCard: Color by mutableStateOf(onCard)
        private set

    public fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        dialog: Color = this.dialog,
        onDialog: Color = this.onDialog,
        gameButtonForegroundNormal: Color = this.gameButtonForegroundNormal,
        gameButtonForegroundPressed: Color = this.gameButtonForegroundPressed,
        gameButtonBackgroundNormal: Color = this.gameButtonBackgroundNormal,
        gameButtonBackgroundPressed: Color = this.gameButtonBackgroundPressed,
        onGameButton: Color = this.onGameButton,
        boardCellNormal: Color = this.boardCellNormal,
        boardCellSelected: Color = this.boardCellSelected,
        boardNumberNormal: Color = this.boardNumberNormal,
        boardNumberSelected: Color = this.boardNumberSelected,
        boardNumberError: Color = this.boardNumberError,
        boardNumberLocked: Color = this.boardNumberLocked,
        switchUncheckedThumb: Color = this.switchUncheckedThumb,
        switchUncheckedTrack: Color = this.switchUncheckedTrack,
        switchCheckedThumb: Color = this.switchCheckedThumb,
        switchCheckedTrack: Color = this.switchCheckedTrack,
        switchBorder: Color = this.switchBorder,
        gamePanelNormal: Color = this.gamePanelNormal,
        gamePanelPressed: Color = this.gamePanelPressed,
        card: Color = this.card,
        onCard: Color = this.onCard,
    ): Colors = Colors(
        primary = primary,
        onPrimary = onPrimary,
        background = background,
        onBackground = onBackground,
        dialog = dialog,
        onDialog = onDialog,
        gameButtonForegroundNormal = gameButtonForegroundNormal,
        gameButtonForegroundPressed = gameButtonForegroundPressed,
        gameButtonBackgroundNormal = gameButtonBackgroundNormal,
        gameButtonBackgroundPressed = gameButtonBackgroundPressed,
        onGameButton = onGameButton,
        boardCellNormal = boardCellNormal,
        boardCellSelected = boardCellSelected,
        boardNumberNormal = boardNumberNormal,
        boardNumberSelected = boardNumberSelected,
        boardNumberError = boardNumberError,
        boardNumberLocked = boardNumberLocked,
        switchUncheckedThumb = switchUncheckedThumb,
        switchUncheckedTrack = switchUncheckedTrack,
        switchCheckedThumb = switchCheckedThumb,
        switchCheckedTrack = switchCheckedTrack,
        switchBorder = switchBorder,
        gamePanelNormal = gamePanelNormal,
        gamePanelPressed = gamePanelPressed,
        card = card,
        onCard = onCard,
    )

    public fun updateColorsFrom(other: Colors) {
        primary = other.primary
        onPrimary = other.onPrimary
        background = other.background
        onBackground = other.onBackground
        dialog = other.dialog
        onDialog = other.onDialog
        gameButtonForegroundNormal = other.gameButtonForegroundNormal
        gameButtonForegroundPressed = other.gameButtonForegroundPressed
        gameButtonBackgroundNormal = other.gameButtonBackgroundNormal
        gameButtonBackgroundPressed = other.gameButtonBackgroundPressed
        onGameButton = other.onGameButton
        boardCellNormal = other.boardCellNormal
        boardCellSelected = other.boardCellSelected
        boardNumberNormal = other.boardNumberNormal
        boardNumberSelected = other.boardNumberSelected
        boardNumberError = other.boardNumberError
        boardNumberLocked = other.boardNumberLocked
        switchUncheckedThumb = other.switchUncheckedThumb
        switchUncheckedTrack = other.switchUncheckedTrack
        switchCheckedThumb = other.switchCheckedThumb
        switchCheckedTrack = other.switchCheckedTrack
        switchBorder = other.switchBorder
        gamePanelNormal = other.gamePanelNormal
        gamePanelPressed = other.gamePanelPressed
        card = other.card
        onCard = other.onCard
    }
}

internal val ThemeColors: Colors = Colors(
    primary = AtomicTangerine,
    onPrimary = Color.White,
    background = AtomicTangerine,
    onBackground = Color.White,
    dialog = Color.White,
    onDialog = Color.Black,
    gameButtonForegroundNormal = HippieBlue,
    gameButtonForegroundPressed = GreenishBlue,
    gameButtonBackgroundNormal = LightNavyBlue,
    gameButtonBackgroundPressed = Elephant,
    onGameButton = Color.White,
    boardCellNormal = Color.White,
    boardCellSelected = DenimBlue,
    boardNumberNormal = Color.Black,
    boardNumberSelected = Color.White,
    boardNumberError = Color.Red,
    boardNumberLocked = Color.Black,
    switchUncheckedThumb = Rope,
    switchUncheckedTrack = Color.White,
    switchCheckedThumb = Color.White,
    switchCheckedTrack = Rope,
    switchBorder = Color.Black,
    gamePanelNormal = Color.White,
    gamePanelPressed = Rope,
    card = Color.White,
    onCard = Color.Black,
)

internal val LocalColors = staticCompositionLocalOf { ThemeColors }
