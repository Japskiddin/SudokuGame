plugins {
    alias(libs.plugins.app.feature.ui)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.game.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.game.uiLogic)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.feature.game.ui.resources"
}
