plugins {
    alias(libs.plugins.app.multiplatform)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.detekt)
}

kotlin {
    android {
        namespace = "io.github.japskiddin.sudoku.core.common"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.immutable)
            implementation(libs.androidx.lifecycle.viewmodel)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            api(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            api(libs.kotlinx.coroutines.swing)
        }
    }
}
