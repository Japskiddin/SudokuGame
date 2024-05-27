// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  dependencies {
    classpath(libs.bundletool)
  }
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.jetbrains.kotlin.android) apply false
  alias(libs.plugins.jetbrains.kotlin.jvm) apply false
  alias(libs.plugins.jetbrains.kotlin.serialization) apply false
  alias(libs.plugins.jetbrains.compose.compiler) apply false
  alias(libs.plugins.google.ksp) apply false
  alias(libs.plugins.dagger.hilt.android) apply false
  alias(libs.plugins.androidx.room) apply false
}

tasks.register("clean", Delete::class) {
  delete(layout.buildDirectory)
}