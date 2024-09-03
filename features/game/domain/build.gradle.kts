import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.feature.domain)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    api(libs.jetbrains.kotlinx.immutable)
}
