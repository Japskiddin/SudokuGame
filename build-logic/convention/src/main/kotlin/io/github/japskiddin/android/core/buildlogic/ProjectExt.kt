package io.github.japskiddin.android.core.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.PluginInstantiationException
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

val Project.commonExtension: CommonExtension<*, *, *, *, *, *>
    get() = applicationExtension
        ?: libraryExtension
        ?: throw PluginInstantiationException("Can only be applied on an android Application or Library")

val Project.applicationExtension: ApplicationExtension?
    get() = extensions.findByType<ApplicationExtension>()

val Project.libraryExtension: LibraryExtension?
    get() = extensions.findByType<LibraryExtension>()

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
