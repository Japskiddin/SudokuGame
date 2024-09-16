import io.github.japskiddin.android.core.buildlogic.Plugins
import io.github.japskiddin.android.core.buildlogic.Versions
import io.github.japskiddin.android.core.buildlogic.applicationExtension
import io.github.japskiddin.android.core.buildlogic.configureBuildTypes
import io.github.japskiddin.android.core.buildlogic.configureJUnit
import io.github.japskiddin.android.core.buildlogic.configureJUnitAndroid
import io.github.japskiddin.android.core.buildlogic.configureKotlin
import io.github.japskiddin.android.core.buildlogic.configureKotlinAndroid
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugin
import io.github.japskiddin.android.core.buildlogic.version
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(libs.plugin(Plugins.AndroidApplication))
                apply(libs.plugin(Plugins.JetbrainsKotlinAndroid))
            }

            requireNotNull(applicationExtension).apply {
                buildFeatures {
                    buildConfig = true
                }

                configureKotlin()
                configureKotlinAndroid(this)
                configureBuildTypes()
                configureJUnitAndroid(this)
                configureJUnit()

                defaultConfig {
                    targetSdk = libs.version(Versions.AndroidSdkTarget).toString().toInt()
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }
            }
        }
    }
}
