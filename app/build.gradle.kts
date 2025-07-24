import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.android.compose)
    alias(libs.plugins.app.hilt)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.test)
    alias(libs.plugins.tracer)
}

android {
    namespace = "io.github.japskiddin.sudoku.game"

    defaultConfig {
        applicationId = "io.github.japskiddin.sudoku.game"
        versionCode = libs.versions.appVersion.code.get().toInt()
        versionName = libs.versions.appVersion.name.get()
        setProperty("archivesBaseName", "sudoku-$versionName-$versionCode")
        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
    }

    val keysRepo: String = if (project.hasProperty("Keys.repo")) {
        project.property("Keys.repo") as String
    } else {
        ""
    }
    val keystoreProperties: Properties? = if (keysRepo.isNotBlank()) {
        val projectPropsFile = file("$keysRepo/google-play-publish.properties")
        if (projectPropsFile.exists()) {
            Properties().apply {
                load(FileInputStream(projectPropsFile))
            }
        } else {
            null
        }
    } else {
        null
    }
    val secretProperties = Properties().apply {
        if (keystoreProperties != null) {
            setProperty(
                "SIGNING_KEYSTORE_PATH",
                file(keysRepo + keystoreProperties["RELEASE_STORE_FILE"]).path
            )
            setProperty(
                "SIGNING_KEYSTORE_PASSWORD",
                keystoreProperties["RELEASE_STORE_PASS"].toString()
            )
            setProperty("SIGNING_KEY_ALIAS", keystoreProperties["RELEASE_KEY_ALIAS"].toString())
            setProperty("SIGNING_KEY_PASSWORD", keystoreProperties["RELEASE_KEY_PASS"].toString())
        } else {
            setProperty(
                "SIGNING_KEYSTORE_PATH",
                file("../keys/android-signing-keystore.jks").path
            )
            setProperty(
                "SIGNING_KEYSTORE_PASSWORD",
                System.getenv("SIGNING_KEYSTORE_PASSWORD")
            )
            setProperty("SIGNING_KEY_ALIAS", System.getenv("SIGNING_KEY_ALIAS"))
            setProperty("SIGNING_KEY_PASSWORD", System.getenv("SIGNING_KEY_PASSWORD"))
        }
    }
    val releaseSigning = signingConfigs.create("release") {
        storeFile = file(secretProperties["SIGNING_KEYSTORE_PATH"] as String)
        keyAlias = secretProperties["SIGNING_KEY_ALIAS"] as String
        storePassword = secretProperties["SIGNING_KEYSTORE_PASSWORD"] as String
        keyPassword = secretProperties["SIGNING_KEY_PASSWORD"] as String
    }

    buildTypes {
        release {
            signingConfig = releaseSigning
        }
        debug {
            signingConfig = releaseSigning
        }
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
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)

    implementation(platform(libs.tracer))
    implementation(libs.bundles.tracer)

    implementation(libs.dagger.hilt.navigation.compose)

    implementation(projects.core.commonAndroid)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.database.data)
    implementation(projects.core.navigation)

    implementation(projects.features.home.ui)
    implementation(projects.features.home.uiLogic)
    implementation(projects.features.home.domain)

    implementation(projects.features.game.ui)
    implementation(projects.features.game.uiLogic)
    implementation(projects.features.game.domain)

    implementation(projects.features.settings.ui)
    implementation(projects.features.settings.uiLogic)
    implementation(projects.features.settings.domain)

    implementation(projects.features.history.ui)
    implementation(projects.features.history.uiLogic)
    implementation(projects.features.history.domain)
}

tracer {
    val localProperties = gradleLocalProperties(rootDir, providers)
    create("defaultConfig") {
        pluginToken = localProperties.getProperty("tracer-pluginToken")
        appToken = localProperties.getProperty("tracer-appToken")

        uploadMapping = true
        uploadNativeSymbols = true
    }
}
