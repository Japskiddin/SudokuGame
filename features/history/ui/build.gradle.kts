plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.compose)
    alias(libs.plugins.app.feature.ui)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.history.ui"

    androidResources {
        enable = true
    }
}

dependencies {
    implementation(projects.features.history.uiLogic)
}
