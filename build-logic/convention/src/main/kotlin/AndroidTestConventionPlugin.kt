package com.nborba.vocalize.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    testOptions {
                        unitTests.all {
                            it.useJUnitPlatform()
                        }
                    }
                    testFixtures {
                        enable = true
                    }
                }
            }

            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    testOptions {
                        unitTests.all {
                            it.useJUnitPlatform()
                        }
                    }
                    testFixtures {
                        enable = true
                    }
                }
            }

            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }

            dependencies {
                // Common test fixtures
                testImplementation(testFixtures(project(":core:common")))

                testImplementation(libs.findLibrary("junit").get())
                testImplementation(libs.findLibrary("junit.jupiter").get())
                testImplementation(libs.findLibrary("mockk").get())
                testImplementation(libs.findLibrary("kotlinx.coroutines.test").get())
                testImplementation(libs.findLibrary("turbine").get())
                testRuntimeOnly(libs.findLibrary("junit.jupiter.engine").get())
                testRuntimeOnly(libs.findLibrary("junit.platformLauncher").get())

                androidTestImplementation(platform(libs.findLibrary("androidx.compose.bom").get()))
                androidTestImplementation(libs.findLibrary("androidx.junit").get())
                androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
                androidTestImplementation(libs.findLibrary("androidx.compose.ui.test.junit4").get())
                androidTestImplementation(libs.findLibrary("androidx.lifecycle.runtime.testing").get())
                debugImplementation(libs.findLibrary("androidx.compose.ui.test.manifest").get())

                add("testFixturesImplementation", platform(libs.findLibrary("androidx.compose.bom").get()))
                add("testFixturesImplementation", libs.findLibrary("androidx.compose.runtime").get())
            }
        }
    }
}
