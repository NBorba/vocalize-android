plugins {
    id("vocalize.android.library")
    id("vocalize.android.compose")
}

android {
    namespace = "com.nborba.vocalize.core.permission"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
}
