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
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }

            dependencies {
                // Design System dependency for all feature modules
                implementation(project(":core:designsystem"))

                ksp(libs.findLibrary("hilt.compiler").get())
                implementation(libs.findLibrary("hilt.android").get())

                implementation(platform(libs.findLibrary("androidx.compose.bom").get()))
                implementation(libs.findLibrary("androidx.activity.compose").get())
                implementation(libs.findLibrary("androidx.compose.material.navigation").get())
                implementation(libs.findLibrary("androidx.compose.material3").get())
                implementation(libs.findLibrary("androidx.compose.ui").get())
                implementation(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())
                implementation(libs.findLibrary("androidx.navigation.compose").get())

                testImplementation(libs.findLibrary("junit").get())
                androidTestImplementation(platform(libs.findLibrary("androidx.compose.bom").get()))
                androidTestImplementation(libs.findLibrary("androidx.compose.ui.test.junit4").get())
                androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
                androidTestImplementation(libs.findLibrary("androidx.junit").get())
                debugImplementation(libs.findLibrary("androidx.compose.ui.tooling").get())
                debugImplementation(libs.findLibrary("androidx.compose.ui.test.manifest").get())


            }
        }
    }
}
