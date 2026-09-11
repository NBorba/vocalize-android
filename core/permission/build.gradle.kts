plugins {
    id("vocalize.android.library")
    id("vocalize.android.compose")
    id("vocalize.android.hilt")
    id("vocalize.android.test")
}

android {
    namespace = "com.nborba.vocalize.core.permission"
}

dependencies {
    // Common extensions and helpers
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    androidTestImplementation(libs.mockk)
}
