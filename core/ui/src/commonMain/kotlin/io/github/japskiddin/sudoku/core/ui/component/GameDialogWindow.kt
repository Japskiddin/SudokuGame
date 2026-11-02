package io.github.japskiddin.sudoku.core.ui.component

import androidx.compose.runtime.Composable

@Composable
internal expect fun GameDialogWindow(onDismiss: () -> Unit, content: @Composable () -> Unit)
