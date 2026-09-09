package com.nborba.vocalize.conventions

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("vocalize.android.library")
                apply("vocalize.android.hilt")
                apply("vocalize.android.test")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                // Design System dependency for all feature modules
                implementation(project(":core:common"))
                implementation(project(":core:designsystem"))

                implementation(platform(libs.findLibrary("androidx.compose.bom").get()))
                implementation(libs.findLibrary("androidx.activity.compose").get())
                implementation(libs.findLibrary("androidx.compose.material.navigation").get())
                implementation(libs.findLibrary("androidx.compose.material3").get())
                implementation(libs.findLibrary("androidx.compose.ui").get())
                implementation(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())
                debugImplementation(libs.findLibrary("androidx.compose.ui.tooling").get())
            }
        }
    }
}
