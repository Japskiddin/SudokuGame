plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.di)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
