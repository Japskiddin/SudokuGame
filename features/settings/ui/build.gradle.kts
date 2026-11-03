plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.settings.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.settings.uiLogic)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.feature.settings.ui.resources"
}
