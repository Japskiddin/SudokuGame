import com.android.build.api.dsl.ApplicationExtension
import io.github.japskiddin.android.core.buildlogic.androidExtension
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.ksp
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.plugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins {
                apply(libs.plugins.google.devtools.ksp.get().pluginId)
            }

            if (androidExtension is ApplicationExtension) {
                pluginManager.apply(libs.plugins.dagger.hilt.android.get().pluginId)
            }

            dependencies {
                implementation(libs.dagger.hilt.android)
                ksp(libs.dagger.hilt.compiler)
            }
        }
    }
}
