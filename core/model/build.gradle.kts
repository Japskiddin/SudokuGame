plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
}

dependencies {
    implementation(projects.core.common)
    api(libs.jetbrains.kotlinx.immutable)
}
