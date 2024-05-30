import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.kotlin.android)
}

kotlin {
  explicitApi = ExplicitApiMode.Strict
  jvmToolchain(17)
}

android {
  namespace = "io.github.japskiddin.sudoku.data"
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
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.javax.inject)
  implementation(project(":core:common"))
  implementation(project(":core:game"))
  implementation(project(":database"))
  implementation(project(":datastore"))
}