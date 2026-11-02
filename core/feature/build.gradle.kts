plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.core.feature"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.compose.components.resources)
            implementation(projects.core.ui)
            api(projects.core.model)
            implementation(projects.core.common)
        }
    }
}
