import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.android.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    implementation(libs.jetbrains.kotlinx.coroutines.android)
    implementation(libs.javax.inject)
}
