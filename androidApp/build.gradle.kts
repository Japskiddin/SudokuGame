import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.app.android.application)
    alias(libs.plugins.app.android.compose)
    alias(libs.plugins.app.di)
    alias(libs.plugins.app.detekt)
    alias(libs.plugins.app.test)
    alias(libs.plugins.tracer)
}

android {
    namespace = "io.github.japskiddin.sudoku.game"

    defaultConfig {
        applicationId = "io.github.japskiddin.sudoku.game"
        versionCode = libs.versions.app.versionCode.get().toInt()
        versionName = libs.versions.app.versionName.get()
        base.archivesName = "sudoku-$versionName-$versionCode"
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
                System.getenv("SIGNING_KEYSTORE_PASSWORD").orEmpty()
            )
            setProperty("SIGNING_KEY_ALIAS", System.getenv("SIGNING_KEY_ALIAS").orEmpty())
            setProperty("SIGNING_KEY_PASSWORD", System.getenv("SIGNING_KEY_PASSWORD").orEmpty())
        }
    }
    val signingStore = file(secretProperties["SIGNING_KEYSTORE_PATH"] as String)
    if (signingStore.exists() && listOf("SIGNING_KEYSTORE_PASSWORD", "SIGNING_KEY_ALIAS", "SIGNING_KEY_PASSWORD")
            .all { secretProperties.getProperty(it).isNotBlank() }
    ) {
        val releaseSigning = signingConfigs.create("release") {
            storeFile = signingStore
            keyAlias = secretProperties.getProperty("SIGNING_KEY_ALIAS")
            storePassword = secretProperties.getProperty("SIGNING_KEYSTORE_PASSWORD")
            keyPassword = secretProperties.getProperty("SIGNING_KEY_PASSWORD")
        }
        buildTypes {
            release { signingConfig = releaseSigning }
            debug { signingConfig = releaseSigning }
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.tracer))
    implementation(libs.bundles.tracer)

    implementation(libs.metro.runtime)
    implementation(libs.metrox.viewmodel)
    implementation(libs.metrox.viewmodel.compose)

    implementation(projects.shared)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.database.data)
}

tracer {
    val localProperties = gradleLocalProperties(rootDir, providers)
    create("defaultConfig") {
        pluginToken = localProperties.getProperty("tracer-pluginToken")
        appToken = localProperties.getProperty("tracer-appToken")

        uploadMapping = true
        uploadNativeSymbols = false
    }
}
