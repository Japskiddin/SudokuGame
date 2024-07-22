plugins {
    alias(libs.plugins.app.android.library)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.game"
}

dependencies {
    implementation(libs.jetbrains.kotlinx.immutable)
}
