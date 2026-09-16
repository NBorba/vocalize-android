plugins {
    id("vocalize.android.library")
    id("vocalize.android.compose")
}

android {
    namespace = "com.nborba.vocalize.core.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.material.icons.extended)
}
