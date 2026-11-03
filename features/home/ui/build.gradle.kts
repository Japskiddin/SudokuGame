plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.home.ui"
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
