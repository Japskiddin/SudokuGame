import com.deezer.caupain.plugin.DependenciesUpdateTask
import com.deezer.caupain.policies.StabilityLevelPolicy

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.multiplatform.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.metro) apply false
    alias(libs.plugins.androidx.room) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.caupain)
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}

tasks.withType<DependenciesUpdateTask> {
    selectIf(StabilityLevelPolicy)
}
