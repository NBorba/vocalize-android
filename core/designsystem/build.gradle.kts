plugins {
    id("vocalize.android.library")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.nborba.vocalize.core.designsystem"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Optional: Core icons if needed
    implementation(libs.androidx.compose.material.icons.core)
}
