plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.di)
}

android {
    namespace = "io.github.japskiddin.sudoku.navigation"
}

dependencies {
    implementation(libs.jetbrains.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.ui.ktx)
}
