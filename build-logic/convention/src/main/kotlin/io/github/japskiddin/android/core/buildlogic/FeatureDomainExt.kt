package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFeatureDomain() {
    dependencies {
        add("api", project(":core:domain"))
        add("api", project(":core:model"))
    }
}
