import io.github.japskiddin.android.core.buildlogic.applicationExtension
import io.github.japskiddin.android.core.buildlogic.configureJUnit
import io.github.japskiddin.android.core.buildlogic.configureKotlin
import io.github.japskiddin.android.core.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            requireNotNull(applicationExtension).apply {
                buildFeatures {
                    buildConfig = true
                }

                configureKotlin()
                configureKotlinAndroid(this)
            }

            configureJUnit()
        }
    }
}
