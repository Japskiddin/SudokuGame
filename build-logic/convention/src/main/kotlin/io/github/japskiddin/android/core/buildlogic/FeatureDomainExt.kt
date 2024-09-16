package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDomain() {
    dependencies {
        implementation(libs.library(Libraries.JavaXInject))
        implementation(findProject(Projects.CoreDomain))
    }
}
