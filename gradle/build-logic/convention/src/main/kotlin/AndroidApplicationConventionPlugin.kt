import com.android.build.api.dsl.ApplicationExtension
import io.github.japskiddin.android.core.buildlogic.configureBuildTypes
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import io.github.japskiddin.android.core.buildlogic.projectJavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.android.application.get().pluginId)
            }

            extensions.getByType(ApplicationExtension::class).apply {
                compileSdk = libs.versions.android.compileSdk.get().toInt()

                defaultConfig {
                    minSdk = libs.versions.android.minSdk.get().toInt()
                    targetSdk = libs.versions.android.targetSdk.get().toInt()
                    vectorDrawables { useSupportLibrary = true }
                    ndk {
                        abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                    }
                }

                compileOptions {
                    sourceCompatibility = projectJavaVersion
                    targetCompatibility = projectJavaVersion
                }

                configureBuildTypes()

                buildFeatures {
                    buildConfig = true
                    resValues = true
                }

                androidResources {
                    @Suppress("UnstableApiUsage")
                    generateLocaleConfig = true
                }

                bundle {
                    language {
                        @Suppress("UnstableApiUsage")
                        enableSplit = false
                    }
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
            }
        }
    }
}
