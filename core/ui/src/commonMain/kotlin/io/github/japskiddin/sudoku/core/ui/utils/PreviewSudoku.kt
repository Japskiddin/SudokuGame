package io.github.japskiddin.sudoku.core.ui.utils

import androidx.compose.ui.tooling.preview.Preview

private const val PORTRAIT = "spec:width=400dp,height=800dp,orientation=portrait"
private const val LANDSCAPE = "spec:width=400dp,height=800dp,orientation=landscape"

@Preview(name = "Portrait", device = PORTRAIT, widthDp = 400, heightDp = 800)
@Preview(name = "Landscape", device = LANDSCAPE, widthDp = 800, heightDp = 400)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.BINARY)
public annotation class PreviewSudoku
