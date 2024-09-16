import io.github.japskiddin.android.core.buildlogic.Plugins
import io.github.japskiddin.android.core.buildlogic.configureJUnit
import io.github.japskiddin.android.core.buildlogic.configureJUnitAndroid
import io.github.japskiddin.android.core.buildlogic.configureKotlin
import io.github.japskiddin.android.core.buildlogic.configureKotlinAndroid
import io.github.japskiddin.android.core.buildlogic.libraryExtension
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply(libs.plugin(Plugins.AndroidLibrary))
                apply(libs.plugin(Plugins.JetbrainsKotlinAndroid))
            }

            requireNotNull(libraryExtension).apply {
                buildFeatures {
                    buildConfig = true
                }

                configureKotlin()
                configureKotlinAndroid(this)
                configureJUnitAndroid(this)
                configureJUnit()
            }
        }
    }
}
