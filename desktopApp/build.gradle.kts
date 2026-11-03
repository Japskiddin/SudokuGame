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

compose.desktop {
    application {
        jvmArgs += "-Dsudoku.version=${libs.versions.app.versionName.get()}"
        mainClass = "io.github.japskiddin.sudoku.desktop.MainKt"
    }
}
