plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.datastore"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.datastore.preferences)
        }
    }
}
