plugins {
  id("java-library")
  alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
  api(libs.jetbrains.kotlin.stdlib)
  api(libs.jetbrains.kotlinx.coroutines.android)
  api(libs.jakatra.inject.api)
}