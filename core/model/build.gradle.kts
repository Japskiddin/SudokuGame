plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.core.model"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            api(libs.kotlinx.immutable)
        }
    }
}
