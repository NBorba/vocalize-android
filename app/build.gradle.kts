plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktlint)
    id("vocalize.android.compose")
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

    // AndroidX & Activity
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Navigation & Serialization
    implementation(libs.androidx.compose.material.navigation)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}
