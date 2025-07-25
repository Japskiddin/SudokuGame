import com.deezer.caupain.model.StabilityLevelPolicy
import com.deezer.caupain.plugin.DependenciesUpdateTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.compose.compiler) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
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
