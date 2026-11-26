import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import io.github.japskiddin.android.core.buildlogic.androidTestImplementation
import io.github.japskiddin.android.core.buildlogic.libs
import io.github.japskiddin.android.core.buildlogic.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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

            pluginManager.withPlugin(libs.plugins.android.multiplatform.library.get().pluginId) {
                extensions.configure<KotlinMultiplatformExtension> {
                    targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                        withHostTestBuilder {}.configure {}
                    }
                }
            }

            configurations.matching { it.name == "testImplementation" }.all {
                dependencies {
                    testImplementation(libs.junit)
                    testImplementation(libs.kotlinx.coroutines.test)
                }
            }

            pluginManager.withPlugin(libs.plugins.kotlin.multiplatform.get().pluginId) {
                extensions.configure<KotlinMultiplatformExtension> {
                    sourceSets.matching { it.name == "jvmTest" }.configureEach {
                        dependencies {
                            implementation(libs.junit)
                        }
                    }
                    sourceSets.matching { it.name == "androidHostTest" }.configureEach {
                        dependencies {
                            implementation(libs.junit)
                        }
                    }
                    sourceSets.named("commonTest") {
                        dependencies {
                            implementation(libs.kotlin.test)
                            implementation(libs.kotlinx.coroutines.test)
                        }
                    }
                }
            }
        }
    }

    private fun Project.configureAndroidTests() {
        dependencies {
            androidTestImplementation(libs.androidx.test.espresso.core)
            androidTestImplementation(libs.androidx.test.ext.junit)
            androidTestImplementation(libs.androidx.arch.core.testing)
            androidTestImplementation(libs.google.truth)
            androidTestImplementation(libs.kotlinx.coroutines.test)
        }
    }
}
