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
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtime.compose)
}
