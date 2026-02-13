plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.feature.domain)
    alias(libs.plugins.app.detekt)
}

dependencies {
    implementation(projects.core.game)
}
