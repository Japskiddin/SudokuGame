import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.multiplatform.compose")
        pluginManager.apply("app.detekt")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.matching { it.name == "androidMain" }.configureEach {
                dependencies {
                    implementation(libs.androidx.core.ktx)
                }
            }
            sourceSets.named("commonMain") {
                dependencies {
                    implementation(libs.androidx.lifecycle.runtime.compose)
                    implementation(libs.androidx.lifecycle.viewmodel.compose)
                    implementation(libs.metrox.viewmodel)
                    implementation(libs.metrox.viewmodel.compose)
                    implementation(project(":core:ui"))
                }
            }
        }
    }
}
