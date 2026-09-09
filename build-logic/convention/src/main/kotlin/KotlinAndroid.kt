package com.nborba.vocalize.conventions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = 37

        defaultConfig.minSdk = 26

        compileOptions.sourceCompatibility = JavaVersion.VERSION_21
        compileOptions.targetCompatibility = JavaVersion.VERSION_21

        // Resolve duplicate resource file conflicts across test and runtime dependencies (e.g., JUnit 5, Coroutines).
        // Merge license/notice files to preserve legal open-source attributions without stripping
        // metadata required by tooling (such as Layout Inspector).
        packaging.resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
            merges += "META-INF/NOTICE.md"
            merges += "META-INF/LICENSE"
            merges += "META-INF/NOTICE"
        }
    }

    extensions.configure<KotlinAndroidProjectExtension> {
        jvmToolchain(21)
    }
}
