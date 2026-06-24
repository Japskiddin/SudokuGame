import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import io.github.japskiddin.android.core.buildlogic.androidTestImplementation
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin(libs.plugins.android.application.get().pluginId) {
                extensions.getByType(ApplicationExtension::class).apply {
                    defaultConfig {
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                }
                configureAndroidTests()
            }

            pluginManager.withPlugin(libs.plugins.android.library.get().pluginId) {
                extensions.getByType(LibraryExtension::class).apply {
                    defaultConfig {
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                }
                configureAndroidTests()
            }

            dependencies {
                testImplementation(libs.junit)
                testImplementation(libs.jetbrains.kotlinx.coroutines.test)
            }
        }
    }

    private fun Project.configureAndroidTests() {
        dependencies {
            androidTestImplementation(libs.androidx.test.espresso.core)
            androidTestImplementation(libs.androidx.test.ext.junit)
            androidTestImplementation(libs.androidx.arch.core.testing)
            androidTestImplementation(libs.google.truth)
            androidTestImplementation(libs.jetbrains.kotlinx.coroutines.test)

            if (pluginManager.hasPlugin(libs.plugins.compose.compiler.get().pluginId)) {
                androidTestImplementation(platform(libs.androidx.compose.bom))
                androidTestImplementation(libs.androidx.compose.ui.test.junit4)
            }
        }
    }
}
