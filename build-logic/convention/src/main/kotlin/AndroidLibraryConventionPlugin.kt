package com.nborba.vocalize.conventions

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jlleitschuh.gradle.ktlint")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                implementation(libs.findLibrary("kotlinx.serialization.json").get())
                implementation(libs.findLibrary("androidx.annotation").get())
                testImplementation(libs.findLibrary("junit").get())
            }
        }
    }
}

