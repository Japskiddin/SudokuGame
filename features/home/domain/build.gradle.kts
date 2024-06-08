import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.ksp)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

android {
    namespace = "io.github.japskiddin.sudoku.feature.home.domain"
    compileSdk = libs.versions.androidSdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidSdk.min.get().toInt()
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.javax.inject)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.jetbrains.kotlinx.coroutines.android)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(projects.core.common)
    api(projects.core.game)
    api(projects.gameData)
    implementation(projects.navigation)
}
