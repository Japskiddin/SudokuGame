plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.compose)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.ui"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
