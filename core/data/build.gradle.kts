plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.data"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(projects.core.common)
            implementation(projects.core.domain)
            implementation(projects.core.model)
            implementation(projects.core.database.data)
            implementation(projects.core.database.entities)
            implementation(projects.core.datastore)
        }
    }
}
