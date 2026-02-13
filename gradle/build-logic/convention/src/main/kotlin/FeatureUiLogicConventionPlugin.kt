import io.github.japskiddin.android.core.buildlogic.api
import io.github.japskiddin.android.core.buildlogic.implementation
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureUiLogicConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.runtime)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.viewmodel.savedstate)
                implementation(libs.jetbrains.kotlinx.coroutines.android)
                implementation(project(":core:common-android"))
                implementation(project(":core:navigation"))
                api(project(":core:model"))
                api(project(":core:feature"))
            }
        }
    }
}
