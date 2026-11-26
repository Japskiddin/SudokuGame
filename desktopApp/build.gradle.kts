plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.app.compose)
    alias(libs.plugins.app.di)
}

dependencies {
    implementation(projects.shared)
    implementation(projects.core.common)
    implementation(compose.desktop.currentOs)
}

val appVersion = libs.versions.app.versionName.get()

tasks.processResources {
    val version = appVersion
    inputs.property("appVersion", version)
    filesMatching("sudoku-version.txt") {
        expand("appVersion" to version)
    }
}

compose.desktop {
    application {
        mainClass = "io.github.japskiddin.sudoku.desktop.MainKt"
    }
}
