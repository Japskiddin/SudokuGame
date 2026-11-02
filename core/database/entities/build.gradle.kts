plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.database.entities"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.room.common)
        }
    }
}
