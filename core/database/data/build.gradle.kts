plugins {
    alias(libs.plugins.app.room)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
    android {
        namespace = "io.github.japskiddin.sudoku.database"
        withDeviceTestBuilder { sourceSetTreeName = "test" }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.database.entities)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.test.ext.junit)
                implementation(libs.androidx.test.runner)
                implementation(libs.androidx.arch.core.testing)
                implementation(libs.google.truth)
            }
        }
    }
}
