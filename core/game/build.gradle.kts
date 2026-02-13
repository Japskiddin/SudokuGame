plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.detekt)
}

dependencies {
    implementation(projects.core.model)
}
