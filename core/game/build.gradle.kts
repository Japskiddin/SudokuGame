plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.kotlin.android)
}

kotlin {
  jvmToolchain(17)
}

android {
  namespace = "io.github.japskiddin.sudoku.core.game"
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
  implementation(project(":core:common"))
}