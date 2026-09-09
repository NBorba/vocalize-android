import org.gradle.kotlin.dsl.testFixturesApi
import org.gradle.kotlin.dsl.testFixturesImplementation

plugins {
    id("vocalize.android.library")
    id("vocalize.android.hilt")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.nborba.vocalize.core.common"

    buildFeatures {
        compose = true
    }
    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)

    testFixturesApi(libs.junit.jupiter)
    testFixturesApi(libs.kotlinx.coroutines.test)
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesImplementation(libs.androidx.compose.runtime)
}
