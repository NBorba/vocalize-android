import org.gradle.kotlin.dsl.testFixturesImplementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    id("vocalize.android.hilt")
    id("vocalize.android.test")
}

android {
    namespace = "com.nborba.vocalize"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.nborba.vocalize"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }

    testFixtures {
        enable = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Modules
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:recorder-api"))
    implementation(project(":feature:recorder-impl"))

    // AndroidX & Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation & Serialization
    implementation(libs.androidx.compose.material.navigation)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Debug & Testing
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(testFixtures(project(":core:common")))

    // TestFixtures
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesImplementation(libs.androidx.compose.runtime)
}
