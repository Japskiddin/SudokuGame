plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.feature.domain)
    alias(libs.plugins.app.detekt)
}

dependencies {
    api(projects.core.game)
}
