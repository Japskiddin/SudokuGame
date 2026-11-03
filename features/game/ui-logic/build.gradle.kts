plugins {
    alias(libs.plugins.app.feature.ui.logic)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.feature.game.ui.logic"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.game.domain)
        }
    }
}
