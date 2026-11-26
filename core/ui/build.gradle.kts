plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.compose)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
    android {
        namespace = "io.github.japskiddin.sudoku.core.ui"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.designsystem)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.navigationevent.compose.multiplatform)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.japskiddin.sudoku.core.ui.resources"
}
