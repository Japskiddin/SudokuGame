import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.jetbrains.kotlinx.immutable)
}
