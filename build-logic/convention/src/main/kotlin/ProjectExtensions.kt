package com.nborba.vocalize.conventions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.implementation(provider: Any) {
    add("implementation", provider)
}

fun DependencyHandler.api(provider: Any) {
    add("api", provider)
}

fun DependencyHandler.ksp(provider: Any) {
    add("ksp", provider)
}

fun DependencyHandler.debugImplementation(provider: Any) {
    add("debugImplementation", provider)
}

fun DependencyHandler.testImplementation(provider: Any) {
    add("testImplementation", provider)
}

fun DependencyHandler.androidTestImplementation(provider: Any) {
    add("androidTestImplementation", provider)
}
