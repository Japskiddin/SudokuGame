plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.multiplatform.compose)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.core.designsystem"
        androidResources.enable = true
    }
    sourceSets {
        commonMain.dependencies {
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.core.designsystem.resources"
}
