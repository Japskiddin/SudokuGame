import com.android.build.api.dsl.ApplicationExtension
import io.github.japskiddin.android.core.buildlogic.androidTestImplementation
import io.github.japskiddin.android.core.buildlogic.debugImplementation
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("app.android.application")
            pluginManager.apply("app.compose")

            pluginManager.withPlugin(libs.plugins.android.application.get().pluginId) {
                extensions.getByType(ApplicationExtension::class).apply {
                    buildFeatures {
                        compose = true
                    }
                }
            }

            dependencies {
                val bom = platform(libs.androidx.compose.bom)
                implementation(bom)
                androidTestImplementation(bom)

                implementation(libs.bundles.compose)

                debugImplementation(libs.androidx.compose.ui.tooling)
                debugImplementation(libs.androidx.compose.ui.test.manifest)
            }
        }
    }
}
