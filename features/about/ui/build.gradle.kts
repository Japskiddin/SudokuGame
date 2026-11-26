plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.about.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.about.uiLogic)
        }
    }
}

compose.resources {
    packageOfResClass = "io.github.japskiddin.sudoku.feature.about.ui.resources"
}
