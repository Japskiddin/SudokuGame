plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.di.metro)
}

dependencies {
    implementation(libs.jetbrains.kotlinx.coroutines.core)
}
