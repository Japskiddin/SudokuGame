package io.github.japskiddin.android.core.buildlogic

import androidx.room.gradle.RoomExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureRoom() {
    extensions.configure<RoomExtension> {
        schemaDirectory("$projectDir/schemas")
    }

    dependencies {
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        ksp(libs.androidx.room.compiler)
    }
}
