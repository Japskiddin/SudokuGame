plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.history.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.history.uiLogic)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.feature.history.ui.resources"
}
