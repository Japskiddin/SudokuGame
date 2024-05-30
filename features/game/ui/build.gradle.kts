import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.kotlin.android)
  alias(libs.plugins.jetbrains.compose.compiler)
  alias(libs.plugins.dagger.hilt.android)
  alias(libs.plugins.google.ksp)
}

kotlin {
  explicitApi = ExplicitApiMode.Strict
}

android {
  namespace = "io.github.japskiddin.sudoku.feature.game.ui"
  compileSdk = libs.versions.androidSdk.compile.get().toInt()

  defaultConfig {
    minSdk = libs.versions.androidSdk.min.get().toInt()
    consumerProguardFiles("consumer-rules.pro")
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  kotlinOptions {
    allWarningsAsErrors = false
    freeCompilerArgs += listOf(
      "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )
  }
}

composeCompiler {
  enableStrongSkippingMode = true
  reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)

  implementation(libs.androidx.core.ktx)
  implementation(libs.javax.inject)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.runtime.livedata)

  implementation(libs.dagger.hilt.navigation.compose)
  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)

  implementation(projects.core.common)
  implementation(projects.core.ui)
  implementation(projects.features.game.domain)

  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
}