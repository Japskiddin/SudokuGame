package io.github.japskiddin.android.core.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = libs.version(Versions.AndroidSdkCompile).toString().toInt()

        defaultConfig {
            minSdk = libs.version(Versions.AndroidSdkMin).toString().toInt()
        }

        compileOptions {
            val javaVersion = JavaVersion.toVersion(libs.version(Versions.Jvm))
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }
}
