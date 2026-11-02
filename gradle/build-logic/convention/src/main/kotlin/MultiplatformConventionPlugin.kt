import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import io.github.japskiddin.android.core.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class MultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.kotlin.multiplatform.get().pluginId)
        pluginManager.apply(libs.plugins.android.multiplatform.library.get().pluginId)
        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()
            jvmToolchain(libs.versions.jvm.get().toInt())
            targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                compileSdk = libs.versions.android.compileSdk.get().toInt()
                minSdk = libs.versions.android.minSdk.get().toInt()
                compilerOptions.jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.get()))
                withHostTestBuilder {}.configure {}
            }
            jvm()
        }
    }
}
