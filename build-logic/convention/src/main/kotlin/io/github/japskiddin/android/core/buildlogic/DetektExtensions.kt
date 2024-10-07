package io.github.japskiddin.android.core.buildlogic

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

internal fun Project.configureDetekt() {
    tasks.named<Detekt>("detekt") {
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }

    extensions.configure<DetektExtension> {
        config.setFrom(rootProject.files("default-detekt-config.yml"))
    }

    dependencies {
        detektPlugins(libs.detekt.formatting)
        if (pluginManager.hasPlugin(libs.plugins.jetbrains.compose.compiler.get().pluginId)) {
            detektPlugins(libs.detekt.rules.compose)
        }
    }
}
