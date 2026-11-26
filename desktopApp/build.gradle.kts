plugins {
    alias(libs.plugins.app.jvm)
    alias(libs.plugins.app.compose)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.detekt)
}

dependencies {
    implementation(projects.shared)
    implementation(projects.core.common)
    implementation(compose.desktop.currentOs)
}

val appVersion = libs.versions.app.versionName.get()
val appIcon = rootProject.file("shared/src/main/ic_launcher-playstore.png")

tasks.processResources {
    val version = appVersion
    inputs.property("appVersion", version)
    filesMatching("sudoku-version.txt") {
        expand("appVersion" to version)
    }
    from(appIcon) {
        rename { "sudoku-icon.png" }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.japskiddin.sudoku.desktop.MainKt"
        nativeDistributions {
            packageName = "Sudoku"
            packageVersion = appVersion
            // DataStore's protobuf serializer uses sun.misc.Unsafe on the JVM.
            modules("jdk.unsupported")
            windows {
                iconFile.set(layout.projectDirectory.file("icons/sudoku.ico"))
            }
            macOS {
                iconFile.set(layout.projectDirectory.file("icons/sudoku.icns"))
            }
            linux {
                iconFile.set(appIcon)
            }
        }
    }
}
