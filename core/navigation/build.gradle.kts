plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.navigation"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
