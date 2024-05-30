import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

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
  namespace = "io.github.japskiddin.sudoku.feature.game.domain"
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
    buildConfig = true
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.javax.inject)

  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)

  implementation(libs.jetbrains.kotlinx.coroutines.android)
  api(libs.jetbrains.kotlinx.immutable)

  implementation(libs.dagger.hilt.android)
  ksp(libs.dagger.hilt.compiler)

  implementation(projects.core.common)
  implementation(projects.core.game)
  implementation(projects.gameData)
  implementation(projects.navigation)
}