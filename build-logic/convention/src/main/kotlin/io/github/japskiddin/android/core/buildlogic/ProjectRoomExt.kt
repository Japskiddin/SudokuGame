package io.github.japskiddin.android.core.buildlogic

import androidx.room.gradle.RoomExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidRoom(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        dependencies {
            implementation(libs.library(Libraries.AndroidXRoomRuntime))
            implementation(libs.library(Libraries.AndroidXRoomKtx))
            ksp(libs.library(Libraries.AndroidXRoomCompiler))
            api(findProject(Projects.CoreDatabaseEntities))
        }
    }
}
