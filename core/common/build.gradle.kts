plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
}

dependencies {
    implementation(libs.jetbrains.kotlinx.coroutines.core)
}
