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
        add("detektPlugins", libs.findLibrary("detekt-formatting").get())
        if (pluginManager.hasPlugin(libs.findPlugin("jetbrains-compose-compiler").get().get().pluginId)) {
            add("detektPlugins", libs.findLibrary("detekt-rules-compose").get())
        }
    }
}