package io.github.japskiddin.android.core.buildlogic

import androidx.room.gradle.RoomExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidRoom(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    pluginManager.run {
        apply("androidx.room")
        apply("com.google.devtools.ksp")
    }

    commonExtension.apply {
        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }
    }

    dependencies {
        add("implementation", libs.findLibrary("androidx-room-runtime").get())
        add("implementation", libs.findLibrary("androidx-room-ktx").get())
        add("ksp", libs.findLibrary("androidx-room-compiler").get())
    }
}
