plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.hilt)
    alias(libs.plugins.app.feature.ui.logic)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.home.ui.logic"
}

dependencies {
    implementation(projects.features.home.domain)
}
