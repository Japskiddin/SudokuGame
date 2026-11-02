plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.app.di)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
    implementation(projects.app)
    implementation(projects.core.common)
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        jvmArgs += "-Dsudoku.version=${libs.versions.app.versionName.get()}"
        mainClass = "io.github.japskiddin.sudoku.desktop.MainKt"
    }
}
