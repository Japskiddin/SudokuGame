import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.multiplatform")
        pluginManager.apply("app.compose")
        pluginManager.apply(libs.plugins.compose.multiplatform.get().pluginId)
        extensions.configure<KotlinMultiplatformExtension> {
            targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                androidResources.enable = true
            }
            sourceSets.named("commonMain") {
                dependencies {
                    api(libs.bundles.compose.multiplatform)
                    api(libs.compose.components.resources)
                    api(libs.androidx.lifecycle.runtime.compose)
                }
            }
        }
    }
}
