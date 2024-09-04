import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    alias(libs.plugins.app.jvm)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict
}

dependencies {
    implementation(libs.androidx.room.common)
}
