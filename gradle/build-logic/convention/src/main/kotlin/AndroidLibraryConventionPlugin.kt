import io.github.japskiddin.android.core.buildlogic.androidConfig
import io.github.japskiddin.android.core.buildlogic.configureKotlinAndroid
import io.github.japskiddin.android.core.buildlogic.configureKotlinJvm
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.android.library.get().pluginId)
                apply(libs.plugins.kotlin.android.get().pluginId)
            }

            androidConfig {
                buildFeatures {
                    buildConfig = true
                }

                configureKotlinJvm()
                configureKotlinAndroid(this)
            }
        }
    }
}
