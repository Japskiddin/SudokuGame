plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.compose)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.core.designsystem"
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.core.designsystem.resources"
}
