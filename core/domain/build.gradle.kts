import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    api(libs.jetbrains.kotlinx.coroutines.core)
    api(projects.core.model)
}
