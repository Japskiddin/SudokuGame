import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.compose.compiler)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "io.github.japskiddin.sudoku.core.ui"
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
        compose = true
        buildConfig = true
    }
}

composeCompiler {
    enableStrongSkippingMode = true
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.unit)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.material3)

    debugApi(libs.androidx.compose.ui.tooling)
    debugApi(libs.androidx.compose.ui.test.manifest)
}
