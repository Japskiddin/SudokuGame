plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    api(projects.core.model)
}
