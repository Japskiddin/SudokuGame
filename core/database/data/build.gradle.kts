plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.database"
        withDeviceTestBuilder { sourceSetTreeName = "test" }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.database.entities)
            implementation(libs.androidx.room.runtime)
        }
        jvmMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
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

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}
