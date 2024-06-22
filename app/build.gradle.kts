import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.android.compose)
    alias(libs.plugins.app.android.hilt)
}

android {
    namespace = "io.github.japskiddin.sudoku.game"

    defaultConfig {
        applicationId = "io.github.japskiddin.sudoku.game"
        versionCode = libs.versions.appVersion.code.get().toInt()
        versionName = libs.versions.appVersion.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        setProperty("archivesBaseName", "sudoku-$versionName-$versionCode")
        vectorDrawables {
            useSupportLibrary = true
        }
        resourceConfigurations += setOf("ru", "en")
        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += setOf("arm64-v8a", "armeabi-v7a")
        }
    }

    val secretProperties = Properties().apply {
        if (project.hasProperty("Keys.repo")) {
            val keyRepo = project.property("Keys.repo") as String
            val projectPropsFile = file("$keyRepo/google-play-publish.properties")
            if (projectPropsFile.exists()) {
                val props = Properties().apply {
                    load(FileInputStream(projectPropsFile))
                }
                setProperty(
                    "SIGNING_KEYSTORE_PATH",
                    file(keyRepo + props["RELEASE_STORE_FILE"]).path
                )
                setProperty(
                    "SIGNING_KEYSTORE_PASSWORD",
                    props["RELEASE_STORE_PASS"].toString()
                )
                setProperty(
                    "SIGNING_KEY_ALIAS",
                    props["RELEASE_KEY_ALIAS"].toString()
                )
                setProperty(
                    "SIGNING_KEY_PASSWORD",
                    props["RELEASE_KEY_PASS"].toString()
                )
            } else {
                setProperty(
                    "SIGNING_KEYSTORE_PATH",
                    file("../android-signing-keystore.jks").path
                )
                setProperty(
                    "SIGNING_KEYSTORE_PASSWORD",
                    System.getenv("SIGNING_KEYSTORE_PASSWORD")
                )
                setProperty(
                    "SIGNING_KEY_ALIAS",
                    System.getenv("SIGNING_KEY_ALIAS")
                )
                setProperty(
                    "SIGNING_KEY_PASSWORD",
                    System.getenv("SIGNING_KEY_PASSWORD")
                )
            }
        } else {
            setProperty(
                "SIGNING_KEYSTORE_PATH",
                file("../android-signing-keystore.jks").path
            )
            setProperty(
                "SIGNING_KEYSTORE_PASSWORD",
                System.getenv("SIGNING_KEYSTORE_PASSWORD")
            )
            setProperty(
                "SIGNING_KEY_ALIAS",
                System.getenv("SIGNING_KEY_ALIAS")
            )
            setProperty(
                "SIGNING_KEY_PASSWORD",
                System.getenv("SIGNING_KEY_PASSWORD")
            )
        }
    }
    val releaseSigning = signingConfigs.create("release") {
        storeFile = file(secretProperties["SIGNING_KEYSTORE_PATH"] as String)
        storePassword = secretProperties["SIGNING_KEYSTORE_PASSWORD"] as String
        keyAlias = secretProperties["SIGNING_KEY_ALIAS"] as String
        keyPassword = secretProperties["SIGNING_KEY_PASSWORD"] as String
    }

    buildTypes {
        release {
            signingConfig = releaseSigning
        }
        debug {
            versionNameSuffix = "-DEBUG"
            signingConfig = releaseSigning
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    kotlinOptions {
        allWarningsAsErrors = false
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }

    lint {
        abortOnError = false
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/kotlin/**",
                "META-INF/androidx.*.version",
                "META-INF/com.google.*.version",
                "META-INF/kotlinx_*.version",
                "kotlin-tooling-metadata.json",
                "DebugProbesKt.bin",
                "META-INF/com/android/build/gradle/*"
            )
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName =
                    "sudoku-${variant.versionName}-${variant.versionCode}-${buildType.name}.apk"
                output.outputFileName = outputFileName
            }
    }
}

tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    compilerArgs.addAll(
        listOf(
            "-Xlint:unchecked",
            "-Xlint:deprecation"
        )
    )
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.dagger.hilt.navigation.compose)

    implementation(projects.core.common)
    implementation(projects.core.game)
    implementation(projects.core.ui)
    implementation(projects.core.datastore)
    implementation(projects.core.database)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.features.home.ui)
    implementation(projects.features.home.domain)
    implementation(projects.features.game.ui)
    implementation(projects.features.game.domain)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
