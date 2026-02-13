plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.app.android.compose)
    alias(libs.plugins.app.detekt)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.ui"

    androidResources {
        enable = true
    }
}

dependencies {
    api(projects.core.designsystem)
}
