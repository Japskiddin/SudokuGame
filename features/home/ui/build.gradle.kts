plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.multiplatform.compose)
    alias(libs.plugins.app.feature.ui)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.home.ui"
        androidResources.enable = true
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.home.uiLogic)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.feature.home.ui.resources"
}
