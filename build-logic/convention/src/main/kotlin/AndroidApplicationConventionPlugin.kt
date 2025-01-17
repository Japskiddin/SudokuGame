import com.android.build.api.dsl.ApplicationExtension
import io.github.japskiddin.android.core.buildlogic.androidConfig
import io.github.japskiddin.android.core.buildlogic.configureBuildTypes
import io.github.japskiddin.android.core.buildlogic.configureKotlinAndroid
import io.github.japskiddin.android.core.buildlogic.configureKotlinJvm
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.android.application.get().pluginId)
                apply(libs.plugins.jetbrains.kotlin.android.get().pluginId)
                apply(libs.plugins.dependency.analysis.get().pluginId)
            }

            androidConfig {
                this as ApplicationExtension

                defaultConfig {
                    targetSdk = libs.versions.androidSdk.target.get().toString().toInt()
                    vectorDrawables { useSupportLibrary = true }
                }

                configureKotlinJvm()
                configureKotlinAndroid(this)
                configureBuildTypes()

                buildFeatures {
                    buildConfig = true
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
            }
        }
    }
}
