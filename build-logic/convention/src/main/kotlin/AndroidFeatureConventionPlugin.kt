package com.nborba.vocalize.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("vocalize.android.library")
                apply("vocalize.android.compose")
                apply("vocalize.android.hilt")
                apply("vocalize.android.test")
            }

            dependencies {
                // Design System & Common dependency for all feature modules
                implementation(project(":core:common"))
                implementation(project(":core:designsystem"))

                // Feature screen level Compose & Navigation
                implementation(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())
                implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.compose.material.navigation").get())
            }
        }
    }
}
