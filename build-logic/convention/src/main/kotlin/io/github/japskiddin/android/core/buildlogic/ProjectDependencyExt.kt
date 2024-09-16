package io.github.japskiddin.android.core.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.support.delegates.DependencyHandlerDelegate

fun DependencyHandlerDelegate.implementation(
    provider: Provider<MinimalExternalModuleDependency>
): Dependency? = add("implementation", provider)

fun DependencyHandlerDelegate.implementation(
    project: Project
): Dependency? = add("implementation", project)

fun DependencyHandlerDelegate.api(
    project: Project
): Dependency? = add("api", project)

fun DependencyHandlerDelegate.detektPlugins(
    provider: Provider<MinimalExternalModuleDependency>
): Dependency? = add("detektPlugins", provider)

fun DependencyHandlerDelegate.ksp(
    provider: Provider<MinimalExternalModuleDependency>
): Dependency? = add("ksp", provider)

fun DependencyHandlerDelegate.androidTestImplementation(
    provider: Provider<MinimalExternalModuleDependency>
): Dependency? = add("androidTestImplementation", provider)

fun DependencyHandlerDelegate.testImplementation(
    provider: Provider<MinimalExternalModuleDependency>
): Dependency? = add("testImplementation", provider)

fun DependencyHandlerDelegate.debugImplementation(
    provider: Provider<MinimalExternalModuleDependency>
): Dependency? = add("debugImplementation", provider)
