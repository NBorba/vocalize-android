import org.gradle.kotlin.dsl.testFixturesApi

plugins {
    id("vocalize.android.library")
    id("vocalize.android.compose")
    id("vocalize.android.hilt")
    id("vocalize.android.test")
}

android {
    namespace = "com.nborba.vocalize.core.common"
}

dependencies {
    // AndroidX & Compose
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // TestFixtures
    testFixturesApi(libs.junit.jupiter)
    testFixturesApi(libs.kotlinx.coroutines.test)
}
