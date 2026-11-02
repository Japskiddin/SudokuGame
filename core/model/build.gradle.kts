import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatform.library)
    alias(libs.plugins.app.test)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict

    jvmToolchain(libs.versions.jvm.get().toInt())

    android {
        namespace = "io.github.japskiddin.sudoku.core.model"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            api(libs.kotlinx.immutable)
        }
    }
}
