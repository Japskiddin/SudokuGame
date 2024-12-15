import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.feature.domain)
    alias(libs.plugins.app.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}
