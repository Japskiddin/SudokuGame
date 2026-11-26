plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.onboarding.ui"
    }
}

compose.resources {
    packageOfResClass = "io.github.japskiddin.sudoku.feature.onboarding.ui.resources"
}
