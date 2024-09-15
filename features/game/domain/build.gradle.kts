import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.feature.domain)
    alias(libs.plugins.app.android.detekt)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    implementation(libs.jetbrains.kotlinx.immutable)
    implementation(projects.core.game)
}
